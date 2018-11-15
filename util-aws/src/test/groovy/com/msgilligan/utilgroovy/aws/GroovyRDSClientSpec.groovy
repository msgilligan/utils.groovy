package com.msgilligan.utilgroovy.aws

import spock.lang.Ignore
import spock.lang.Shared

/**
 *
 */
class GroovyRDSClientSpec extends BaseAWSClientSpec {
    static final dbInstanceId = 'xxx'

    @Shared
    GroovyRDSClient rds

    def setup() {
        rds = new GroovyRDSClient(credentialsProvider, regionEnum)
        assert rds != null
    }

    def "can list instances"() {
        when:
        def list = rds.listDBInstances()

        then:
        list != null
        list.size() >= 0
    }

    def "can list snapshots"() {
        when:
        def list = rds.listDBSnapshots()

        then:
        list != null
        list.size() >= 10
    }

    def "can list snapshots for a particular DB"() {
        when:
        def list = rds.listDBSnapshots(dbInstanceId)

        then:
        list != null
        list.size() >= 3
    }

    def "can find latest backup for a particular DB"() {
        when:
        def list = rds.listDBSnapshots(dbInstanceId)

        then:
        list != null
        list.size() >= 3

        when:
        def latest = list.max{ it.snapshotCreateTime }

        then:
        latest != null
    }

    def "can make a snapshot"() {
        when:
        def snapshotId = 'delete-me-test-' + UUID.randomUUID().toString()
        def snapshot = rds.createSnapshot(dbInstanceId, snapshotId)

        then:
        snapshot != null
        snapshot.DBInstanceIdentifier == dbInstanceId

        when:
        def latest = rds.listDBSnapshots(dbInstanceId).max { it.snapshotCreateTime }

        then:
        latest.DBInstanceIdentifier == dbInstanceId
    }

    def "can restore from snapshot"() {
        given:
        def dbSourceInstanceId = dbInstanceId
        def dbRestoreInstanceId = 'test-restored-' + dbSourceInstanceId + '-' + UUID.randomUUID().toString()

        when:
        def latest = rds.listDBSnapshots(dbSourceInstanceId).max{ it.snapshotCreateTime }.DBSnapshotIdentifier
        def newInstance = rds.newInstanceFromSnapshot(latest, dbRestoreInstanceId)

        then:
        newInstance != null
    }

    @Ignore("Takes too long")
    def "can create and delete instance"() {
        def instanceID = 'deleteme'
        def username = "unittestuser"
        def password = "deletemepleasehurryupdeletemenow"

        when: "we create an instance"
        def instance = rds.createDBInstance(instanceID, null, username, password, true)

        then: "instance is valid"
        instance.engine == "PostgreSQL"

        when: "we delete it"
        instance = rds.deleteDBInstance(instanceID)

        then:
        instance.getDBInstanceStatus() == "goingaway"
    }

}
