package com.msgilligan.utilgroovy.aws.tools

import com.msgilligan.utilgroovy.aws.GroovyEC2Client
import com.msgilligan.utilgroovy.aws.VagrantBox

/**
 * Associate an AWS Vagrant box with an Elastic IP Address
 * Currently uses AWS_DEFAULT_REGION environment variable for setting region
 */
class AssociateAddress {
    static void main(String... args) {
        if (args.size() != 2) {
            println "Usage: associateAddress.groovy <machineName> <address>"
            System.exit(-1)
        }
        def machineName = args[0]
        def address = args[1]

        println "Getting EC2 instance ID for ${machineName} from Vagrant..."

        def instanceId = new VagrantBox(machineName).getEC2InstanceId()

        println "Creating EC2 client..."

        def ec2 = new GroovyEC2Client()

        println "Associating ${machineName} with ${address}..."

        def result = ec2.associateAddress(instanceId, address)

        println "Result: ${result}"
        System.exit(0)  // TODO: Set exit code from result
    }
}
