package com.msgilligan.utilgroovy.aws

import spock.lang.Ignore
import spock.lang.Shared

/**
 *
 */
class GroovyRDSClientSpec extends BaseAWSClientSpec {

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
