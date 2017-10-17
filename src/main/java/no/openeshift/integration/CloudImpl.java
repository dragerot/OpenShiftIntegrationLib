package no.openeshift.integration;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceList;
import io.fabric8.openshift.api.model.ProjectRequest;
import io.fabric8.openshift.client.OpenShiftClient;

import java.util.Map;

public class CloudImpl implements Cloud {
    OpenShiftClient client;

    public CloudImpl(OpenShiftClient client) {
        this.client = client;
    }

    @Override
    public NamespaceList listNamespaces() {
        return client.namespaces().list();
    }

    @Override
    public Namespace getNamespaceResourceWithname(String name) {
        return client.namespaces().withName(name).get();
    }

    @Override
    public void deleteNamespace(String name) {
        client.namespaces().withName(name).delete();
    }

    @Override
    public Namespace createNewLabelInNameSpace(String nameSpaceName, String labelKey, String labelName) {
        return client.namespaces().withName(nameSpaceName).edit()
                .editMetadata()
                .addToLabels(labelKey, labelName)
                .endMetadata()
                .done();
    }

    @Override
    public ProjectRequest creatNewNameSpace(
            String nameSpaceName,
            String description,
            String displayName) {
        ProjectRequest request = null;
        try {
            request = client
                    .projectrequests()
                    .createNew()
                    .withNewMetadata()
                    .withName(nameSpaceName)
                    .endMetadata()
                    .withDescription(description)
                    .withDisplayName(displayName).done();
        } finally {
            if (request != null) {
                client.projects().withName(request.getMetadata().getName()).delete();
            }
        }
        return request;
    }

    @Override
    public ServiceList listServices() {
        return client.services().list();
    }

    @Override
    public ServiceList listServicesInNameSpace(String name) {
        return client.services().inNamespace(name).list();
    }

    @Override
    public Service getServiceWithname(String inNameSpaceName, String name) {
        return client.services().inNamespace(inNameSpaceName).withName(name).get();
    }

    @Override
    public void deleteService(String inNameSpaceMame, String name) {
        client.services().inNamespace(inNameSpaceMame).withName(name).delete();
    }

    @Override
    public Service createService(String name, Map<String, String> labels) {
        return null;
    }
}
