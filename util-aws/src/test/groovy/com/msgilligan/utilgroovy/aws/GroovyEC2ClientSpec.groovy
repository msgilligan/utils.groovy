package com.msgilligan.utilgroovy.aws

import spock.lang.Shared

/**
 *
 */
class GroovyEC2ClientSpec extends BaseAWSClientSpec {

    @Shared
    GroovyEC2Client ec2

    def setup() {
        ec2 = new GroovyEC2Client(credentialsProvider, regionEnum)
        assert ec2 != null
    }
    
    def "can list regions"() {
        when:
        def list = ec2.listRegions()

        then:
        list != null
        list.size() > 5
    }

}
