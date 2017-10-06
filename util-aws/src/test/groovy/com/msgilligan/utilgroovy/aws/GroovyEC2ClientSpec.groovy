package com.msgilligan.utilgroovy.aws

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import spock.lang.Shared
import spock.lang.Specification

/**
 *
 */
class GroovyEC2ClientSpec extends Specification {

    @Shared
    GroovyEC2Client ec2

    def setup() {
        String accessKey = System.getenv("AWS_ACCESS_KEY");
        String secretKey = System.getenv("AWS_SECRET_KEY");
        AWSCredentials cred = new BasicAWSCredentials(accessKey, secretKey);
        ec2 = new GroovyEC2Client(cred)
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
