package com.msgilligan.utilgroovy.aws

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider
import com.amazonaws.regions.Regions
import spock.lang.Shared
import spock.lang.Specification

/**
 * Base Specification for AWS SDK client class testing
 */
abstract class BaseAWSClientSpec extends Specification {
    @Shared
    AWSCredentialsProvider credentialsProvider
    
    @Shared
    Regions regionEnum

    def setup() {
        credentialsProvider = new EnvironmentVariableCredentialsProvider()
        regionEnum = Regions.US_WEST_2
    }
}
