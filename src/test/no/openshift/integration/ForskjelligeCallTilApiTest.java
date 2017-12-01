package no.openshift.integration;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.extensions.Deployment;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.openshift.api.model.DeploymentConfig;
import io.fabric8.openshift.api.model.DeploymentConfigList;
import io.fabric8.openshift.api.model.DeploymentTriggerImageChangeParamsBuilder;
import io.fabric8.openshift.api.model.ProjectRequest;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;
import no.openeshift.integration.Fabric8Util;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;

@DisplayName("CreateNameSpace")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ForskjelligeCallTilApiTest {
    private final String URL = "https://192.168.99.100:8443";
    private final String PROJECT = "ftp-developer";
    private static final Logger logger = LoggerFactory.getLogger(ForskjelligeCallTilApiTest.class);

    KubernetesClient client;
    OpenShiftClient osClient = null;

    @BeforeAll
    public void before() {
        Config config = new ConfigBuilder()
                .withMasterUrl(URL)
                //.withOauthToken("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJldHByb3NqZWt0Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZWNyZXQubmFtZSI6ImRlcGxveWVyLXRva2VuLTczeHdnIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6ImRlcGxveWVyIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQudWlkIjoiNWViZTBmYzctZDUwZS0xMWU3LWI2NGMtYzI2ODhjZGQyZTZiIiwic3ViIjoic3lzdGVtOnNlcnZpY2VhY2NvdW50OmV0cHJvc2pla3Q6ZGVwbG95ZXIifQ.pXX_QWsDq3LXyD943y_mXTB4pXcvjVxjiESZzv0CfNp60PA1Kl8iIBiDEpTiRVqHu8b03Fx1JnT5yuhgNumyI1-4Dc2E9NE86AUZNdY--RYeBXZp9OJ60tlm4C2YI13g0nvDpfbvNau_Zc2bPATaoOAN5H7QF2C1rj_3jhXC0YxhgE-FojeWN9RXHW-kAY6F40HcNK8jW6eUNybCVJX3Fk2yPYZqzb5h1RHenjY4IF4pSBXpeACjX1H1KpMhUgyM-k2hWGIhG-yL9OJjRHxSu57daJGYsaqDFtqf8lRYxlZAd7BfHWxAj7Awmps1PKkeIXWwngBEYLT75v8M1svuSA")
                .withUsername("system:admin")
               // .withPassword("developer")
                .build();
        client = new DefaultKubernetesClient(config);
        osClient = new DefaultOpenShiftClient(config);// client.adapt(OpenShiftClient.class);

    }

    @AfterAll
    public void after() {
        client.close();
        osClient.close();
    }

    //@Test
    public void createProjectTest() {
        createProject();
    }

    /**
     * https://github.com/fabric8io/kubernetes-client/blob/master/kubernetes-examples/src/main/java/io/fabric8/openshift/examples/NewProjectExamples.java
     * https://github.com/fabric8io/kubernetes-client
     */
    private ProjectRequest createProject() {
        Map<String, String> labelsApp = new HashMap<String, String>();
        labelsApp.put("app", "app1");

        ProjectRequest request = null;
        try {
            request = osClient
                    .projectrequests()
                    .createNew()
                    .withNewMetadata()
                    .withName(PROJECT)
                    .withLabels(labelsApp)
                    .endMetadata()
                    .withDescription("PROJECT-desc")
                    .withDisplayName("JPROJECT-desc-displayname")
                    .done();

            osClient.namespaces().withName(PROJECT).edit()
                    .editMetadata()
                    .addToLabels(labelsApp)
                    .endMetadata()
                    .done();
            logger.info("Created deployment",request);
        } catch (Exception e) {

        } finally {
            if (request != null) {

                // osClient.projects().withName(request.getMetadata().getName()).delete();
            }
        }
        return request;
    }

    //@Test
    public void deleteNameSpace() {
        osClient.projects().withName(PROJECT).delete();
    }

    @Test
    public void listProjects() {
        Service service = new Service();

        //osClient.services().inNamespace("etprosjekt").createOrReplace();
        PodList podList =osClient.pods().inNamespace("etprosjekt").list();

        ServiceList myServices = osClient.services().list();
        NamespaceList myNs = osClient.namespaces().list();
        for (Namespace ns : myNs.getItems()) {
            System.out.println("ns " + ns.toString());
            //ns.c
        }
        for (Service item : myServices.getItems()) {
            System.out.println(item.toString());
        }
    }

    @Test
    public void deploymentTest(){
        //String PROSJEKTNAVN="ftp-developer";
        String PROSJEKTNAVN="etprosjekt";
        //String PROSJEKTNAVN="js2i-demo";
        ProjectRequest projectRequest = createProject();  //js2i-demo:js2i-service-account etprosjektserviceccount
        //ServiceAccount developer = new ServiceAccountBuilder().withNewMetadata().withName("js2i-service-account").endMetadata().build();

        DeploymentConfigList deploymentConfigList = osClient.deploymentConfigs().list();

      // try{
           //osClient.serviceAccounts().inNamespace(PROSJEKTNAVN).createOrReplace(developer);

           DeploymentConfig deploymentConfig =
                   osClient.deploymentConfigs()
                           //.createOrReplaceWithNew()
                           .inNamespace(PROSJEKTNAVN)
                           .createOrReplaceWithNew()
                           .withNewMetadata()
                           .withName("toregardtestutv")
                           .withNamespace(PROSJEKTNAVN)
                           //.addToLabels("app","toregardTest")
                           .endMetadata()
                           .withNewSpec()
                           .withReplicas(1)
                           .addToSelector("app","toregardTest")
                           .withNewTemplate()
                           .withNewMetadata()
                           .addToLabels("name","toregardTest")
                           .endMetadata()
                           .withNewSpec()
                           .addNewContainer()
                           .withName("containernavntoregardtest")
                           .withImage("toregard/jerseydemo1@sha256:0c3c838bf65ebb5850e71e226dc8aa93642295e7bcc27ad007b0f148b854153f")
                           //.withImage("penshift/origin-ruby-sample")
                           .withImagePullPolicy("Always")
                           .addNewPort()
                           .withContainerPort(8083)
                           .withProtocol("TCP")
                           .endPort()
                           .endContainer()
                           .endSpec()
                           .endTemplate()
                           .endSpec()
                           .done();


//       }catch (Exception e){
//
//       }


    }


    @Test
    public void deployment() {
        try {
//            ProjectRequest projectRequest = new ProjectRequestBuilder()
//                    .withNewMetadata()
//                    .withName(PROJECT)
//                    .addToLabels("project", "thisisatest")
//                    .endMetadata()
//                    .build();
//            osClient.projectrequests().create(projectRequest);

//
//            ProjectRequest projectRequest = createProject();
//
//            ServiceAccount fabric8 = new ServiceAccountBuilder().withNewMetadata().withName("fabric8").endMetadata().build();
//
//            // client.serviceAccounts().inNamespace(PROJECT).createOrReplace(fabric8);
//
//           DeploymentConfig deploymentConfig =osClient.deploymentConfigs().inNamespace(PROJECT).createOrReplaceWithNew()
//                    .withNewMetadata()
//                    .withName("frontend")
//                    .endMetadata()
//                    .withNewSpec()
//                     .withNewTemplate()
//                      .withNewMetadata()
//                        .addToLabels("name","frontend")
//                      .endMetadata()
//                    .withNewSpec()
//                      .addNewContainer()
//                        .withName("helloworld")
//                        .withImage("openshift/origin-ruby-sample")
//                        .withPorts(Fabric8Util.createContainerPort("TCP",8080))
//                     .endContainer()
//                    .endSpec()
//                   .endTemplate()
//                   .withReplicas(5)
//                   .addNewTrigger()
//                     .withType("ConfigChange")
//                     .withType("ImageChange")
//                        .withNewImageChangeParams()
//                            .withAutomatic(true)
//                            .withContainerNames("helloworld")
//                            .withFrom(Object)
//                        .endImageChangeParams()
//
//                   .endTrigger()
//
//                   .addNewTrigger()
//
//                   .endSpec()
//
//                    .addNewTrigger()
//                    .withType("ConfigChange")
//                    .endTrigger()
//                    .addToSelector("app", "nginx")
//                    .withNewTemplate()
//                    .withNewMetadata()
//                    .addToLabels("app", "nginx")
//                    .endMetadata()
//                    .withNewSpec()
//                    .addNewContainer()
//                    .withName("nginx")
//                    .withImage("nginx")
//                    .addNewPort()
//                    .withContainerPort(80)
//                    .endPort()
//                    .endContainer()
//                    .endSpec()
//                    .endTemplate()
//                    .endSpec()
//                    .done();
//            logger.info("Created deployment",deploymentConfig);
//
//            osClient.deploymentConfigs().inNamespace(PROJECT).withName("nginx").scale(2, true);
//            //Created pods
//            osClient.pods().inNamespace(PROJECT).list().getItems();
//            osClient.deploymentConfigs().inNamespace(PROJECT).withName("nginx").delete();
//            osClient.replicationControllers().inNamespace(PROJECT).list().getItems();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertThat("Feiler", true);

        }
    }


}

/*
 DeploymentConfig deploymentConfig =osClient.deploymentConfigs().inNamespace(PROJECT).createOrReplaceWithNew()
                    .withNewMetadata()
                    .withName("nginx")
                    .endMetadata()
                    .withNewSpec()
                    .withReplicas(1)
                    .addNewTrigger()
                    .withType("ConfigChange")
                    .endTrigger()
                    .addToSelector("app", "nginx")
                    .withNewTemplate()
                    .withNewMetadata()
                    .addToLabels("app", "nginx")
                    .endMetadata()
                    .withNewSpec()
                    .addNewContainer()
                    .withName("nginx")
                    .withImage("nginx")
                    .addNewPort()
                    .withContainerPort(80)
                    .endPort()
                    .endContainer()
                    .endSpec()
                    .endTemplate()
                    .endSpec()
                    .done();
 */