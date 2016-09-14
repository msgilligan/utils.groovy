package com.msgilligan.utilgroovy.aws.tools

import com.msgilligan.utilgroovy.aws.GroovyEC2Client

/**
 * List EC2 regions
 */
class ListRegions {
    static void main(String... args) {
        def ec2 = new GroovyEC2Client()
        ec2.listRegions()
    }
}