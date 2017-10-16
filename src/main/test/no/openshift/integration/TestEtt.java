package no.openshift.integration;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;
import org.junit.jupiter.api.*;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("A special test case")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestEtt {
    public static final String HOST_URL = "https://192.168.0.16:8443";


    OpenShiftClient target = null;

    @BeforeAll
    public void before() {
        Config config = new ConfigBuilder()
                .withMasterUrl(HOST_URL)
                //.withOauthToken("ea4d0859-b1d7-11e7-bb08-8a91c37e8cd3").build();
                .withUsername("developer")
                .withPassword("developer").build();
        DefaultOpenShiftClient defaultOpenShiftClient = new DefaultOpenShiftClient(config);
       target = defaultOpenShiftClient.adapt(OpenShiftClient.class);

    }

    @AfterAll
    public void after() {
        target = null;
    }

    @Test
    public void hallo() {
        assertNotNull(target);
        URL url = target.getOpenshiftUrl();
        assertNotNull(url);
        assertEquals(url.getPort(), 8443);

        Namespace myns = target.namespaces().withName("detteprj").edit()
                .editMetadata()
                .addToLabels("a", "label")
                .endMetadata()
                .done();

        //NamespaceList myNs = target.namespaces().list();

//
//        ServiceList myServices = target.services().list();
//
//        ServiceList myNsServices = target.services().inNamespace("default").list();
    }
}
