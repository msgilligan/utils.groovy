package com.msgilligan.utilgroovy.aws

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.services.ec2.AmazonEC2
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder
import com.amazonaws.services.ec2.model.AssociateAddressRequest
import com.amazonaws.services.ec2.model.Region
import groovy.transform.CompileStatic

/**
 * Simple Groovy wrapper for AmazonEC2Client
 */
@CompileStatic
class GroovyEC2Client {
    AmazonEC2 ec2

    /**
     * Default constructor uses environment variable credentials
     */
    GroovyEC2Client() {
        this(new EnvironmentVariableCredentialsProvider())
    }

    /**
     * Construct with any instance of AWSCredentials
     */
    GroovyEC2Client(AWSCredentialsProvider credentialsProvider) {
        this(credentialsProvider, Regions.DEFAULT_REGION)
    }

    GroovyEC2Client(AWSCredentialsProvider credentialsProvider, Regions regionEnum) {
        ec2 = AmazonEC2ClientBuilder
                .standard()
                .withCredentials(credentialsProvider)
                .withRegion(regionEnum)
                .build()
    }
    
    /**
     * Associate an Elastic IP address with an Amazon instance
     * Roughly equivalent to:
     * ec2-associate-address -i ${instanceId} ${address} --region us-west-2".execute().waitForProcessOutput(System.out, System.err)
     * @param instanceId Amazon EC2 instance ID
     * @param address Pre-allocated Elastic IP address
     * @return
     */
    def associateAddress(String instanceId, String address) {
        def req = new AssociateAddressRequest(instanceId, address)
        def result = ec2.associateAddress(req)
        return result
    }

    List<Region> listRegions() {
        def result = ec2.describeRegions()
        result.regions.each { region ->
            println "${region.regionName}"
        }
    }
}
