package com.msgilligan.utilgroovy.aws

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.services.rds.AmazonRDS
import com.amazonaws.services.rds.AmazonRDSClientBuilder
import com.amazonaws.services.rds.model.CreateDBInstanceRequest
import com.amazonaws.services.rds.model.DBInstance
import com.amazonaws.services.rds.model.DeleteDBInstanceRequest

/**
 * Wrapper for AmazonRDSClient
 */
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

    public DBInstance createDBInstance(String instanceID, String securityGID, String username, String password, Boolean publicIP) {
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

    public List<DBInstance> listDBInstances() {
        def result = rds.describeDBInstances()

        def list = result.getDBInstances()

        list.each {
            println "${it.getDBInstanceIdentifier()}: ${it.getDBInstanceStatus()}"
            println ""
        }
        
        return list
    }

}
