package no.openeshift.integration;

import io.fabric8.openshift.client.OpenShiftClient;

public class CloudImpl implements Cloud{
    OpenShiftClient kubernetesClient;

    public CloudImpl(OpenShiftClient openShiftClient){
        this.kubernetesClient=kubernetesClient;
    }

    public void createApplication(String appName) {
        //kubernetesClient
    }
}
