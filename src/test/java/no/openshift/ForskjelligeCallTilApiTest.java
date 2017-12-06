package no.openshift;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.openshift.api.model.DeploymentConfig;
import io.fabric8.openshift.api.model.DeploymentConfigBuilder;
import io.fabric8.openshift.api.model.DeploymentConfigList;
import io.fabric8.openshift.api.model.ProjectRequest;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;

public class ForskjelligeCallTilApiTest {
    //192.168.99.100
    private final String URL = "https://openshift:8443";
    //    private final String URL = "https://atlas-utv.vegvesen.no:8443";
    private final String PROJECT = "ftp-developer";
    private static final Logger logger = LoggerFactory.getLogger(ForskjelligeCallTilApiTest.class);
    private static final String TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJmamVybm1lZ3Byb2plY3QiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlY3JldC5uYW1lIjoiZGVwbG95ZXItdG9rZW4tNmxsY2MiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC5uYW1lIjoiZGVwbG95ZXIiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiJjNDM2ODBkMi1kOGM5LTExZTctOTIwMy1hZWFkZTFkMmMwNzkiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6Zmplcm5tZWdwcm9qZWN0OmRlcGxveWVyIn0.gl4Uvn6_xq0OGFHGkrPpc7yuacIIiaekINjoBTi_Sz8mqRlWoR0xIr8gSQDc3CPL2KPLCZLbUg6kfeaHJmDu4ctdZYTc7XakJcYvV0evI9RoV6ZJPy-63OpJIggmpaFCGvYxRV_oqNUO6GwsyhSBrT8bOz6lrYM5fbYdi68VjWLIMSuLcnMtPu61vo3kzSLmMZc8DxJ8EtC-oJZmdh8KjMCZwIG6cZg2_4Z4RKMLfrQGaoPEcqz_2QJTZ1D5QdUVp2dmd_tJGTQTtu1v6sZc8ynG_n3YQOH7mJOmEm8DUew1TUueCucdznKU4bxm8nZPKGZ2iVzBRri92aLdRwfbRw";

    //OpenShiftClient osClient = null;

//    //@Before
//    public void before() {
//        String TOKEN_fjernmegproject = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJmamVybm1lZ3Byb2plY3QiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlY3JldC5uYW1lIjoiZGVwbG95ZXItdG9rZW4tNmxsY2MiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC5uYW1lIjoiZGVwbG95ZXIiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiJjNDM2ODBkMi1kOGM5LTExZTctOTIwMy1hZWFkZTFkMmMwNzkiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6Zmplcm5tZWdwcm9qZWN0OmRlcGxveWVyIn0.gl4Uvn6_xq0OGFHGkrPpc7yuacIIiaekINjoBTi_Sz8mqRlWoR0xIr8gSQDc3CPL2KPLCZLbUg6kfeaHJmDu4ctdZYTc7XakJcYvV0evI9RoV6ZJPy-63OpJIggmpaFCGvYxRV_oqNUO6GwsyhSBrT8bOz6lrYM5fbYdi68VjWLIMSuLcnMtPu61vo3kzSLmMZc8DxJ8EtC-oJZmdh8KjMCZwIG6cZg2_4Z4RKMLfrQGaoPEcqz_2QJTZ1D5QdUVp2dmd_tJGTQTtu1v6sZc8ynG_n3YQOH7mJOmEm8DUew1TUueCucdznKU4bxm8nZPKGZ2iVzBRri92aLdRwfbRw";
//        Config config = new ConfigBuilder()
//                .withMasterUrl(URL)
//                //.withOauthToken(TOKEN_fjernmegproject)
//                .withUsername("developer")
//                .withPassword("developer")
//
//                .build();
//        // client = new DefaultKubernetesClient(config);
//        osClient = new DefaultOpenShiftClient(config);// client.adapt(OpenShiftClient.class);
//
//    }

//    // @After
//    public void after() {
//        // client.close();
//        osClient.close();
//    }

    @Test
    public void virkerTest() {
        Assert.assertTrue(true);
    }

    @Test
    public void createProjectTest() {
        OpenShiftClient osClient = getOpenShiftClient("developer", "developessr", "");
        createProject("aaaaaa",osClient);
        osClient.close();

    }

    @Test
    public void listDeplymentConfigTest() {
        OpenShiftClient osClient = getOpenShiftClient("developer", "developer", "");
        DeploymentConfigList deploymentConfigList = osClient.deploymentConfigs().list();
        osClient.close();

    }


    @Test
    public void deleteNameSpace() {
        OpenShiftClient osClient = getOpenShiftClient("", "", "");
        osClient.projects().withName(PROJECT).delete();
    }

    @Test
    public void listDiverserojectsTest() {
        OpenShiftClient client = getOpenShiftClient("developer", "developer", "");
        client.deploymentConfigs().list();
        //osClient.services().inNamespace("etprosjekt").createOrReplace();
        PodList podList = client.pods().inNamespace("etprosjekt").list();
        PodList podList2 = client.pods().inNamespace("fjernmegproject").list();

        ServiceList myServices = client.services().list();
        NamespaceList myNs = client.namespaces().list();
        for (Namespace ns : myNs.getItems()) {
            System.out.println("ns " + ns.toString());
            //ns.c
        }
        for (Service item : myServices.getItems()) {
            System.out.println(item.toString());
        }
    }


    /**
     * TEST SOM MAA VIRKE
     */
    @Test
    public void deployment_nginx_Test() {
        String PROSJEKTNAVN = "fjernmegproject";
        String DEPLOYMENTCONFIG_NAME= "nginx-deployment";
        String IMAGE_NAME = "nginx:1.13.7";
        String APP_NAME = "nginx";
        String KJORE_MILJO ="atm";
        String API_VERSION="v1";
        int PORT =8083;
        int TARGET_PORT =8085;
        String TARGET_PORT_PROTOCOL="tcp";

        try {
            OpenShiftClient osClient = getOpenShiftClient("developer", "developer", "");
            System.out.println("****CREATE DEPLOYMENTCONFIG**************************");
             osClient.deploymentConfigs().create(DeploymentFacade.createDeplymentConfig(PROSJEKTNAVN,DEPLOYMENTCONFIG_NAME,IMAGE_NAME,APP_NAME,PORT,API_VERSION));
             System.out.println("****CREATE SERVICE**************************");
             osClient.services().create(DeploymentFacade.createService(PROSJEKTNAVN,PORT,TARGET_PORT,TARGET_PORT_PROTOCOL,APP_NAME,API_VERSION));
             System.out.println("****CREATE ROUTE**************************");
             //https://docs.openshift.org/latest/dev_guide/routes.html
             osClient.routes().create(DeploymentFacade.createRoute(PROSJEKTNAVN,APP_NAME,KJORE_MILJO,TARGET_PORT,TARGET_PORT_PROTOCOL,API_VERSION));
        } catch (Exception e) {
            System.out.println("*****deployment_nginx_Test***************ERROR" + e.getMessage());
        }
    }


    @Test
    public void CreateDeploymentConfigfraInternett() {
        OpenShiftClient osClient = getOpenShiftClient("developer", "developer", "");
        String PROSJEKTNAVN = "fjernmegproject";

        DeploymentConfigList liste = osClient.deploymentConfigs().list();
        for (DeploymentConfig item : liste.getItems()) {
            System.out.println(item.getMetadata().getName());
        }
        osClient.deploymentConfigs().inNamespace(PROSJEKTNAVN).createOrReplaceWithNew()
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
                .withNewStatus()
                .withLatestVersion(1l)
                .endStatus()
                .done();

    }


    @Test
    public void deploymentTest() {
        OpenShiftClient osClient = getOpenShiftClient("developer", "developer", "");
        String PROSJEKTNAVN = "fjernmegproject";

        //Får "User "system:anonymous" cannot list all projectrequests.project.openshift.io in the cluster",
        //ProjectRequest projectRequest = createProject(PROSJEKTNAVN,osClient);  //js2i-demo:js2i-service-account etprosjektserviceccount

        //ServiceAccount developer = new ServiceAccountBuilder().withNewMetadata().withName("js2i-service-account").endMetadata().build();
        //DeploymentConfigList deploymentConfigList = osClient.deploymentConfigs().list();
        //osClient.serviceAccounts().inNamespace(PROSJEKTNAVN).createOrReplace(developer);
        int a = 0;
        try {
            DeploymentConfigList l = osClient.deploymentConfigs()
                    .inNamespace(PROSJEKTNAVN)
                    .list();
            for (DeploymentConfig item : l.getItems()) {
                System.out.println(item.getMetadata().getName());
            }

//            osClient.deploymentConfigs()
//                    .inNamespace(PROSJEKTNAVN)
//                    .create()

            DeploymentConfig deploymentConfig;
//                osClient.deploymentConfigs()
//                         .createOrReplaceWithNew()
//                        .inNamespace(PROSJEKTNAVN)
//                        .createOrReplaceWithNew()
//                        .withNewMetadata()
//                        .withName("toregardtestutv")
//                        .withNamespace(PROSJEKTNAVN)
//                        //.addToLabels("app","toregardTest")
//                        .endMetadata()
//                        .withNewSpec()
//                        .withReplicas(1)
//                        .addToSelector("app", "toregardTest")
//                        .withNewTemplate()
//                        .withNewMetadata()
//                        .addToLabels("name", "toregardTest")
//                        .endMetadata()
//                        .withNewSpec()
//                        .addNewContainer()
//                        .withName("containernavntoregardtest")
//                        .withImage("toregard/jerseydemo1@sha256:0c3c838bf65ebb5850e71e226dc8aa93642295e7bcc27ad007b0f148b854153f")
//                        //.withImage("penshift/origin-ruby-sample")
//                        .withImagePullPolicy("Always")
//                        .addNewPort()
//                        .withContainerPort(8083)
//                        .withProtocol("TCP")
//                        .endPort()
//                        .endContainer()
//                        .endSpec()
//                        .endTemplate()
//                        .endSpec()
//                        .done();


        } catch (Exception e) {
            String b = e.getMessage();
        }


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

    private OpenShiftClient getOpenShiftClient(String user, String passord, String oauthToken) {
        Config config = new ConfigBuilder().withMasterUrl(URL).build();
        config.setTrustCerts(true);
        if (oauthToken.trim().length() > 0) {
            config.setOauthToken(oauthToken);
        } else {
            config.setUsername(user);
            config.setPassword(passord);

        }
        //KubernetesClient kubernetesClient = new DefaultKubernetesClient(config);
        //OpenShiftClient client = kubernetesClient.adapt(OpenShiftClient.class);
        return new DefaultOpenShiftClient(config);

    }

    private ProjectRequest createProject(String projectname, OpenShiftClient osClient) {
        Map<String, String> labelsApp = new HashMap<String, String>();
        labelsApp.put("app", "app1");
        ProjectRequest request = null;
        request = osClient
                .projectrequests()
                .createNew()
                .withNewMetadata()
                .withName(projectname)
                .withLabels(labelsApp)
                .endMetadata()
                .withDescription(projectname + "-desc")
                .withDisplayName(projectname + "displayname")
                .done();

        osClient.namespaces().withName(projectname).edit()
                .editMetadata()
                .addToLabels(labelsApp)
                .endMetadata()
                .done();
        logger.info("Created deployment", request);
        return request;
    }

    private DeploymentConfig createDeplymentConfig() {
        String PROSJEKTNAVN = "fjernmegproject";
        String DEPLOYMENT_CONFIG_NAME = "nginx-deployment";
        String IMAGE_NAME = "nginx:1.13.7";
        String CONTAINER_NAME = "nginx";
        Map<String, String> SPEC_MAP = new HashMap<String, String>();
        SPEC_MAP.put("app", "nginx");

        DeploymentConfig deploymentConfig = new DeploymentConfigBuilder()
                .withNewMetadata()
                .addToLabels("app", "nginx")
                .withName(DEPLOYMENT_CONFIG_NAME)
                .withNamespace(PROSJEKTNAVN)
                .endMetadata()
                .withNewSpec()
                .withReplicas(1)
                .addToSelector("app", "nginx")
                .withNewTemplate()
                .withNewMetadata()
                .addToLabels("app", "nginx")
                .endMetadata()
                .withNewSpec()
                .addNewContainer()
                .withImage(IMAGE_NAME)
                .withName(CONTAINER_NAME)
                .addNewPort()
                .withContainerPort(8083)
                .withProtocol("TCP")
                .endPort()
                .endContainer()
                .endSpec()
                .endTemplate()
                .endSpec().build();

        return deploymentConfig;
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