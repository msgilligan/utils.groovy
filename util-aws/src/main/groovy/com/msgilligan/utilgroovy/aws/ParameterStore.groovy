package com.msgilligan.utilgroovy.aws

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult

/**
 * Wrapper for AWSSimpleSystemsManagement
 */
class ParameterStore {
    private AWSSimpleSystemsManagement client
    private AWSCredentialsProvider credentialsProvider

    ParameterStore() {
        this(new EnvironmentVariableCredentialsProvider())
    }

    ParameterStore(AWSCredentialsProvider provider) {
        this(provider, Regions.DEFAULT_REGION)
    }

    ParameterStore(AWSCredentialsProvider provider, Regions regionEnum) {
        credentialsProvider = provider
        client = AWSSimpleSystemsManagementClientBuilder
                .standard()
                .withCredentials(credentialsProvider)
                .withRegion(regionEnum)
                .build()
    }

    public ParameterStore(AWSCredentials cred) {
        this(new AWSStaticCredentialsProvider(cred))
    }

    String getStringParam(String name, boolean withDecryption) {
        def paramRequest = new GetParameterRequest()
        paramRequest.setName(name)
        paramRequest.setWithDecryption(withDecryption)
        paramRequest.setRequestCredentialsProvider(credentialsProvider)
        GetParameterResult result = client.getParameter(paramRequest)
        return result.getParameter().getValue()
    }
}
