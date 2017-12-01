package no.openshift.integration;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.*;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.dsl.ExtensionsAPIGroupDSL;
import io.fabric8.openshift.api.model.ProjectRequest;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;
import io.fabric8.openshift.client.OpenShiftConfig;
import org.junit.jupiter.api.*;

import javax.validation.constraints.AssertTrue;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("A special test case")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestEtt {
    public static final String HOST_URL = "https://192.168.0.16:8443";

    KubernetesClient client;
    OpenShiftClient osClient = null;

    @BeforeAll
    public void before() {
        NamedAuthInfo na; //= OpenShiftConfig;
        OpenShiftConfig.builder().build();
        Config config = new ConfigBuilder()
                //.withMasterUrl(HOST_URL)
                //.withOauthToken()
                //            .withOauthToken("ea4d0859-b1d7-11e7-bb08-8a91c37e8cd3").build();
                .withUsername("developer")
                .withPassword("developer").build();

       client= new DefaultKubernetesClient(config);
       osClient = new DefaultOpenShiftClient(config);// client.adapt(OpenShiftClient.class);

    }

    @AfterAll
    public void after() {
        client.close();
        osClient.close();
    }

    @Test
    public void hallo() {
//        assertTrue(true);
//        System.out.println("cli1 " + client.getMasterUrl() + " " + client.getNamespace());
//        System.out.println("cli2 " + osClient.getMasterUrl() + " " + osClient.getNamespace());
//
//        ServiceList myServices = osClient.services().list();
//        NamespaceList myNs = osClient.namespaces().list();
//        //kybernetes tillater ikke dette
//        // NamespaceList myNs2 = client.namespaces().list();
//        for (Namespace ns : myNs.getItems()) {
//            System.out.println("ns " + ns.toString());
//            //ns.c
//        }
//        for (Service item : myServices.getItems()) {
//            System.out.println(item.toString());
//        }
//
//        ExtensionsAPIGroupDSL e = osClient.extensions();
//        //osClient.oAuthClients()

        ProjectRequest request = null;


//        Namespace myns = osClient
//                .namespaces().withName("halloproject1").edit()
//                .editMetadata()
//                .addToLabels("verdi1", "DetteErVerdi")
//                .endMetadata()
//                .done();

//                Namespace myns = osClient.namespaces().withName("Name1").createNew()
//                .editMetadata()
//                .addToLabels("a", "label")
//                //.withNamespace("namespace1")
//                .endMetadata()
//                .done();



//        Service myservice = client.services().inNamespace("default").createNew()
//                .editMetadata()
//                .withName("myservice")
//                .addToLabels("another", "label")
//                .endMetadata()
//                .done();
    }
}
