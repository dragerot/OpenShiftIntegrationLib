package no.openshift.integration;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceList;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;
import no.openeshift.integration.Cloud;
import no.openeshift.integration.CloudImpl;
import org.junit.jupiter.api.*;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("A special test case")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestEtt {
    public static final String HOST_URL = "https://192.168.0.16:8443";


    OpenShiftClient client = null;
    Cloud target;

    @BeforeAll
    public void before() {
        Config config = new ConfigBuilder()
                .withMasterUrl(HOST_URL)
                //.withOauthToken("ea4d0859-b1d7-11e7-bb08-8a91c37e8cd3").build();
                .withUsername("developer")
                .withPassword("developer").build();
        DefaultOpenShiftClient defaultOpenShiftClient = new DefaultOpenShiftClient(config);
        client = defaultOpenShiftClient.adapt(OpenShiftClient.class);
        target = new CloudImpl(client);

    }

    @AfterAll
    public void after() {
        target = null;
    }

   // @Test
    public void listNameSpaceTest() {
        assertNotNull(client);
        assertNotNull(target);

        NamespaceList myNs = target.listNamespaces();
        myNs.getItems().forEach(
                (Namespace k) ->
                        System.out.println(k)
        );

    }

    //@Test
    public void deleteNameSpaceTest() {
         target.deleteNamespace("test");
        assertTrue(true);
    }

    @Test
    public void createnameSpaceTest() {
        target.creatNewNameSpace("toregardSpace","beskrivelse","displaynavnet");
        assertTrue(true);
    }

//    @Test
//    public void createnameSpaceTest() {
//        Map<String,String > labels = new HashMap<>();
//        labels.put("nokke1","Hallo1");
//        labels.put("nokke2","Hallo2");
//        Namespace myNs = target.creatNewNameSpace("test",labels);
//        assertNotNull(myNs);
//    }

}
