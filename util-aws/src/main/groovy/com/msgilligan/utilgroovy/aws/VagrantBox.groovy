package com.msgilligan.utilgroovy.aws

import groovy.json.JsonSlurper

/**
 * Wrapper for vagrant command-line tool
 */
class VagrantBox {
    String machineName;

    VagrantBox(String machineName) {
        this.machineName = machineName;
    }

    String getEC2InstanceId() {
        def json = "vagrant awsinfo -p -m ${machineName}".execute().text
        def info = new JsonSlurper().parseText(json)
        String instanceId = info.instance_id
        return instanceId
    }
}
