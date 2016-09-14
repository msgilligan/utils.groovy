package com.msgilligan.utilgroovy.aws

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.services.ec2.AmazonEC2Client
import com.amazonaws.regions.Region
import com.amazonaws.services.ec2.model.AssociateAddressRequest

/**
 * Simple Groovy wrapper for AmazonEC2Client
 */
class GroovyEC2Client {
    def credentials
    def ec2

    /**
     * Default constructor uses environment variable credentials
     */
    GroovyEC2Client() {
        credentials = new EnvironmentVariableCredentialsProvider().getCredentials()
        ec2 = new AmazonEC2Client(credentials)
    }

    GroovyEC2Client setRegion(Regions region) {
        ec2.region = Region.getRegion(region)
        return this
    }

    /**
     * Associate an Elastic IP address with an Amazon instance
     * Roughly equivalent to:
     * ec2-associate-address -i ${instanceId} ${address} --region us-west-2".execute().waitForProcessOutput(System.out, System.err)
     * @param instanceId Amazon EC2 instance ID
     * @param address Pre-allocated Elastic IP address
     * @return
     */
    def associateAddress(instanceId, address) {
        def req = new AssociateAddressRequest(instanceId, address)
        def result = ec2.associateAddress(req)
        return result
    }

    void listRegions() {
        def result = ec2.describeRegions()
        result.regions.each { region ->
            println "${region.regionName}"
        }
    }
}
