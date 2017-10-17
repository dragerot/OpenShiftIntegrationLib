package no.openeshift.integration;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceList;
import io.fabric8.openshift.api.model.ProjectRequest;

import java.util.Map;

public interface Cloud {
    NamespaceList listNamespaces();
    Namespace getNamespaceResourceWithname(String name);
    void deleteNamespace(String name);
    Namespace createNewLabelInNameSpace(String nameSpaceName,String labelKey,String labelName);
    ProjectRequest creatNewNameSpace(String nameSpaceName,String description,String displayName);

    ServiceList listServices();
    ServiceList listServicesInNameSpace(String name);
    Service getServiceWithname(String inNameSpaceName,String name);
    void deleteService(String inNameSpaceMame,String name);
    Service createService(String name,Map<String,String> labels);


}
