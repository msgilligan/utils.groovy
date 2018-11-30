package com.msgilligan.utilgroovy.aws

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.services.rds.AmazonRDS
import com.amazonaws.services.rds.AmazonRDSClientBuilder
import com.amazonaws.services.rds.model.CreateDBInstanceRequest
import com.amazonaws.services.rds.model.CreateDBSnapshotRequest
import com.amazonaws.services.rds.model.DBInstance
import com.amazonaws.services.rds.model.DBSnapshot
import com.amazonaws.services.rds.model.DeleteDBInstanceRequest
import com.amazonaws.services.rds.model.DescribeDBInstancesRequest
import com.amazonaws.services.rds.model.DescribeDBSnapshotsRequest
import com.amazonaws.services.rds.model.ModifyDBInstanceRequest
import com.amazonaws.services.rds.model.RestoreDBInstanceFromDBSnapshotRequest
import groovy.transform.CompileStatic

/**
 * Wrapper for AmazonRDSClient
 */
@CompileStatic
class GroovyRDSClient {
    private AmazonRDS rds

    GroovyRDSClient() {
        this(new EnvironmentVariableCredentialsProvider())
    }

    GroovyRDSClient(AWSCredentialsProvider credentialsProvider) {
        this(credentialsProvider, Regions.DEFAULT_REGION)
    }

    GroovyRDSClient(AWSCredentialsProvider credentialsProvider, Regions regionEnum) {
        println "Creating RDS client..."
        rds = AmazonRDSClientBuilder
                .standard()
                .withCredentials(credentialsProvider)
                .withRegion(regionEnum)
                .build()
    }

    DBInstance createDBInstance(String instanceID, String securityGID, String username, String password, Boolean publicIP) {
        def req = new CreateDBInstanceRequest()
                .withDBInstanceIdentifier(instanceID)
                .withEngine("postgres")
                .withEngineVersion("9.6.3")
                .withDBInstanceClass("db.t2.small")
                .withAllocatedStorage(5)
                .withPubliclyAccessible(publicIP)
                .withVpcSecurityGroupIds(securityGID)
                .withBackupRetentionPeriod(0)
                .withMasterUsername(username)
                .withMasterUserPassword(password)

        def instance = rds.createDBInstance(req)
        println instance
        return instance
    }

    DBInstance deleteDBInstance(String instanceID) {
        DeleteDBInstanceRequest req = new DeleteDBInstanceRequest(instanceID)
                .withSkipFinalSnapshot(true)
        def result = rds.deleteDBInstance(req)
        println result
        return result
    }

    List<DBInstance> listDBInstances() {
        def result = rds.describeDBInstances()

        def list = result.getDBInstances()

        list.each {
            println "${it.getDBInstanceIdentifier()}: ${it.getDBInstanceStatus()}"
            println ""
        }
        
        return list
    }

    DBInstance describeDBInstance(String dbInstanceID) {
        def req = new DescribeDBInstancesRequest()
            .withDBInstanceIdentifier(dbInstanceID)
        def result = rds.describeDBInstances(req)

        DBInstance instance = result.getDBInstances()[0]
        return instance
    }

    List<DBSnapshot> listDBSnapshots(String dbInstanceIdentifier = null) {
        def req = new DescribeDBSnapshotsRequest()

        if (dbInstanceIdentifier) {
            req.DBInstanceIdentifier = dbInstanceIdentifier
        }

        def result = rds.describeDBSnapshots(req)

        def list = result.getDBSnapshots()

        list.each {
            println "${it.DBSnapshotIdentifier}: ${it.DBInstanceIdentifier} at ${it.snapshotCreateTime}"
            println ""
        }

        return list
    }

    DBSnapshot createSnapshot(String dbInstanceIdentifier, String dbSnapshotIdentifier = null) {
        def req = new CreateDBSnapshotRequest()
            .withDBInstanceIdentifier(dbInstanceIdentifier)
        if (dbSnapshotIdentifier) {
            req.setDBSnapshotIdentifier(dbSnapshotIdentifier)
        }
        rds.createDBSnapshot(req)
    }

    DBInstance newInstanceFromSnapshot(String snapshotID, String newInstanceID) {
        def req = new RestoreDBInstanceFromDBSnapshotRequest()
            .withDBSnapshotIdentifier(snapshotID)
            .withDBInstanceIdentifier(newInstanceID)
        return rds.restoreDBInstanceFromDBSnapshot(req)
    }

    boolean addSecurityGroup(String instanceID, String securityGroupName) {
        ModifyDBInstanceRequest req = new ModifyDBInstanceRequest()
            .withDBInstanceIdentifier(instanceID)
            .withVpcSecurityGroupIds(securityGroupName)
            .withApplyImmediately(true)
        rds.modifyDBInstance(req)
    }

    /**
     * Copy the (first) VPC Security group from one DB instance to another
     * This is needed after newInstanceFromSnapshot to enable access
     * 
     * @param sourceId Id of source DB instance
     * @param destID Id of destination DB instance
     * @return
     */
    boolean copySecurityGroup(String sourceId, String destID) {
        DBInstance source = this.describeDBInstance(sourceId)
        String vpcSecurityGroupId = source.vpcSecurityGroups[0].vpcSecurityGroupId
        println "Secgroup = $vpcSecurityGroupId"
        this.addSecurityGroup(destID, vpcSecurityGroupId)
        return true
    }

    String pgURLForIdentifier(String dbInstanceIdentifier) {
        DBInstance instance = this.describeDBInstance(dbInstanceIdentifier)
        return "postgresql://${instance.masterUsername}@${instance.endpoint.address}:${instance.endpoint.port}/${instance.DBName}"
    }

    /* TODO: pgDumpInstance */

}
