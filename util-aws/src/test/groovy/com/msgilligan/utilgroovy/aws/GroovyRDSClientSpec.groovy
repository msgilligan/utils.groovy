package com.msgilligan.utilgroovy.aws

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import spock.lang.Shared
import spock.lang.Specification

/**
 *
 */
class GroovyRDSClientSpec extends Specification {

    @Shared
    GroovyRDSClient rds

    def setup() {
//        String accessKey = System.getenv("AWS_ACCESS_KEY");
//        String secretKey = System.getenv("AWS_SECRET_KEY");
//        AWSCredentials cred = new BasicAWSCredentials(accessKey, secretKey);
        rds = new GroovyRDSClient()
        assert rds != null
    }

    def "can list instances"() {
        when:
        def list = rds.listDBInstances()

        then:
        list != null
        list.size() >= 0
    }

}
