package no.openshift.integration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import no.openeshift.integration.Fabric8Util;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

//import io.fabric8.docker.client.Config;
//import io.fabric8.docker.client.ConfigBuilder;
//import io.fabric8.docker.client.DefaultDockerClient;
//import io.fabric8.docker.client.DockerClient;
//import io.fabric8.docker.dsl.container.LimitSinceBeforeSizeFiltersAllRunningInterface;
//tga import io.fabric8.kubernetes.api.Controller;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.ContainerPort;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.api.model.ServiceList;
import io.fabric8.kubernetes.api.model.ServicePort;
import io.fabric8.kubernetes.api.model.ServiceStatus;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.openshift.api.model.Build;
import io.fabric8.openshift.api.model.BuildConfig;
import io.fabric8.openshift.api.model.BuildConfigBuilder;
import io.fabric8.openshift.api.model.BuildRequestBuilder;
import io.fabric8.openshift.api.model.DeploymentConfig;
import io.fabric8.openshift.api.model.DeploymentConfigBuilder;
import io.fabric8.openshift.api.model.DeploymentConfigStatus;
import io.fabric8.openshift.api.model.ImageStream;
import io.fabric8.openshift.api.model.ImageStreamBuilder;
import io.fabric8.openshift.api.model.WebHookTriggerBuilder;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;
//import no.test.DockerUtil;


//import com.openshift.client.IOpenShiftConnection;
//import com.openshift.client.IUser;
//import com.openshift.client.OpenShiftConnectionFactory;

public class Tester {
    //public IOpenShiftConnection connection;
    KubernetesClient client;
    OpenShiftClient osClient;
    String project = "myproject";

    @Before
    public void setUp() {
        io.fabric8.kubernetes.api.model.Config conf;
        io.fabric8.kubernetes.client.Config conf2;
        //connection = new OpenShiftConnectionFactory().getConnection("my_user_application", "user", "password");
        conf = new io.fabric8.kubernetes.api.model.ConfigBuilder()
                .build();
        conf2 = new io.fabric8.kubernetes.client.ConfigBuilder()
                //.with
                .withUsername("developer")
                .withPassword("developer")
        		/*
           		.withUsername("system:admin")
        		.withPassword("admin")
        		*/
                .build();
        client = new DefaultKubernetesClient(conf2);
        osClient = new DefaultOpenShiftClient(conf2);

        //osClient.
        System.out.println("cli1 " + client.getMasterUrl() + " " + client.getNamespace());
        System.out.println("cli2 " + osClient.getMasterUrl() + " " + osClient.getNamespace());
        //osClient.
        System.out.println("setup done");
    }

    @After
    public void after() {
        client.close();
        osClient.close();
        System.out.println("after test");
        //IUser user = user = connection.getUser();
    }

    @Test
    public void myTest() {
        ServiceList myServices = osClient.services().list();
        NamespaceList myNs = osClient.namespaces().list();
        for (Namespace ns : myNs.getItems()) {
            System.out.println("ns " + ns.toString());
            //ns.c
        }
        for (Service item : myServices.getItems()) {
            System.out.println(item.toString());
        }
    /*
    PodList myNsServices = client.pods().inNamespace(project).list();
    for (Pod item : myNsServices.getItems()) {
    	//System.out.println("iy " + item.get);
    	//item.
    }
    List<ImageStream> imagestr = osClient.imageStreams().list().getItems();
    */
        //tga Config config4 = new ConfigBuilder().withDockerUrl("https://192.168.99.102:2376").build();

       //tga DockerClient dClient = new DefaultDockerClient(config4);
        //LimitSinceBeforeSizeFiltersAllRunningInterface<List<Container>> conts = dClient.container().list();
    /*
    for (Container i : conts.running()) {
    	System.out.println("id " + i.getId());
    }
    ContainerInspect inspect = dClient.container().withName("").inspect();
    System.out.println(inspect);
  */
        Boolean myservice = client.pods().inNamespace(project).withName("classify").delete();
        System.out.println("d " + myservice);
        myservice = client.services().inNamespace(project).withName("classify-1").delete();
        System.out.println("d " + myservice);
        myservice = client.pods().inNamespace(project).withName("classify-1-build").delete();
        System.out.println("d " + myservice);
        myservice = client.pods().inNamespace(project).withName("predict-1-build").delete();
        System.out.println("d " + myservice);
        myservice = client.pods().inNamespace(project).withName("stockstat-1-build").delete();
        System.out.println("d " + myservice);
        System.out.println("test done");
    }

    @Test
    public void mytest2() {
        String name = "tensorflow-predict";
        String debian = "debian";
        String debianstretch = "debian:stretch";
        String stretch = "stretch";
        Map<String, String> selector = new HashMap<String, String>();
        selector.put("app", name);
        selector.put("deploymentconfig", name);
        Map<String, String> labels = new HashMap<String, String>();
        labels.put("build", name);
        ImageStream isDebian = new ImageStreamBuilder()
                .withNewMetadata()
                .withName(debian)
                .addToLabels("build", debian)
                .withLabels(labels)
                .endMetadata()
                .withNewSpec()
                .addNewTag()
                .addToAnnotations("openshift.io/imported-from", debianstretch)
                .editOrNewFrom()
                .withKind("DockerImage")
                .withName(debianstretch)
                .endFrom()
                .withName(stretch)
                .endTag()
                .endSpec()
                .build();
        ImageStream isTFPredict = new ImageStreamBuilder()
                .withNewMetadata()
                .withName(name)
                .addToLabels("build", debian)
                .withLabels(labels)
                .endMetadata()
                .withNewSpec()
                .endSpec()
                .build();
        Boolean sd = osClient.imageStreams().inNamespace(project).withName("debian").delete();
        System.out.println("s " + sd);
        String s = osClient.imageStreams().inNamespace(project).create(isDebian).getStatus().toString();
        System.out.println("s " + s);
        Boolean sd2 = osClient.imageStreams().inNamespace(project).withName("tensorflow-predict").delete();
        System.out.println("s " + sd2);
        String s2 = osClient.imageStreams().inNamespace(project).create(isTFPredict).getStatus().toString();
        System.out.println("s " + s2);
        //System.out.println("r " + osClient.imageStreams().inNamespace(project).withName("debian").isReady() + " " + osClient.imageStreams().inNamespace(project).withName("tensorflow-predict").isReady());
        BuildConfig bc = new BuildConfigBuilder()
                .withNewMetadata()
                .withName(name)
                .addToLabels("build", name)
                .endMetadata()
                .withNewSpec()
                .withNewOutput()
                .withNewTo()
                .withKind("ImageStreamTag")
                .withName(name + ":latest")
                .endTo()
                .endOutput()
                .withNewSource()
                .withDockerfile("/tmp/tensorflow/predict/Dockerfile")
                //.withType("Binary")
                .withType("Dockerfile")
                .endSource()
                .withNewStrategy()
                .withNewDockerStrategy()
                .withNewFrom()
                .withKind("ImageStreamTag")
                .withName(debianstretch)
                .endFrom()
                .endDockerStrategy()
                .withType("Docker")
                .endStrategy()
                .addNewTrigger()
                .withType("imagechange")
                .withNewImageChange()
                .withNewFrom()
                .withKind("ImageStreamTag")
                .withName(debianstretch)
                .endFrom()
                /*
                .withNewGeneric()
                .withSecret("secret101")
                .endGeneric()
                */
                .endImageChange()
                .endTrigger()
                .endSpec()
                .build();
        System.out.println("del " + osClient.buildConfigs().inNamespace(project).withName(bc.getMetadata().getName()).delete());
        osClient.buildConfigs().inNamespace(project).createOrReplace(bc);
        osClient.buildConfigs().inNamespace(project).withName(bc.getMetadata().getName()).instantiate(new BuildRequestBuilder()
                .withNewMetadata().withName(bc.getMetadata().getName()).endMetadata()
                .build());
        //osClient.buildConfigs().inNamespace(project).withName(bc.getMetadata().getName())
        //.withSecret("secret101")
        //.withType("imagechange")
        //.tr
        /*
        .trigger(new WebHookTriggerBuilder()
        		.withSecret("secret101")
                .build());
                */
        ImageStream is = new ImageStreamBuilder().withNewMetadata().withName("myIs").endMetadata()
                .build();
        //osClient.replicationControllers().inNamespace(project).create(arg0);
        /*
        client.r
        BuildConfig bc = new BuildConfigBuilder().withNewMetadata().withName("myBc").endMetadata().build();
    	Service service = new ServiceBuilder()
    	          .withNewMetadata()
    	              .withName("myservice")
    	          .endMetadata()
    	          .withNewSpec()
    	            .addNewPort()
    	              .withProtocol("TCP")
    	              .withPort(80)
    	              .withNewTargetPort(8080)
    	            .endPort()
    	            .addToSelector("key1", "value1")

    	           //withPortalIP("172.30.234.134")
    	            .withType("ClusterIP")
    	          .endSpec()
    	          .build();
    	//ImageConfiguration image = new ImageConfigurationBuilder();
    	//service.         .b build(image);
    	new ImageConfiguration.Builder()
        .name(projectName)
        .buildConfig(new BuildImageConfiguration.Builder()
                .from(projectName)
                .build()
).build();
*/
    }

//    @Test
//    public void t3() {
//        DockerUtil.method();
//    }

    public void createDC(String name, String image) {

        Map<String, String> labelsApp = new HashMap<String,String>();
        labelsApp.put("app", name);
        labelsApp.put("deploymentconfig", name);
        /*
        Map<String, String> labelsApp2 = new HashMap<>();
        labelsApp2.put(name, project);
        */
        ObjectMeta metaApp = Fabric8Util.createObjectMeta(name, labelsApp);
        //ObjectMeta metaApp = Fabric8Util.createObjectMeta(name);


        Map<String, String> selector = new HashMap<String, String>();
        selector.put("app", name);
        selector.put("deploymentconfig", name);
/*
        Map<String, String> selector2 = new HashMap<>();
        selector2.put(name, project);
 */
        ContainerPort containerPort = Fabric8Util.createContainerPort("TCP", 8001);
        List<ContainerPort> ports = new ArrayList<ContainerPort>();
        ports.add(containerPort);
        List<String> names = new ArrayList<String>();
        names.add(name);
        Container container = Fabric8Util.createContainer(name, image, ports);
        List<Container> containers = new ArrayList<Container>();
        containers.add(container);

        ServicePort servicePort = Fabric8Util.createServicePort("TCP", 8001, 8001);
        System.out.println("here4");
        Service srv = new ServiceBuilder()
                .withMetadata(metaApp)
                .withNewSpec()
                .withPorts(servicePort)
                .withSelector(selector)
                .endSpec()
                .build();

        DeploymentConfig dc = new DeploymentConfigBuilder()
                .withMetadata(metaApp)
                .withNewSpec()
                .withReplicas(1)
                .withSelector(selector)
                .withNewStrategy()
                .withNewResources()
                .endResources()
                .endStrategy()
                .withNewTemplate()
                .withNewMetadata()
                .withLabels(labelsApp)
                .endMetadata()
                .withNewSpec()
                //.withServiceAccountName(name)
                .withContainers(containers)
                .endSpec()
                .endTemplate()
                .withTest(false)
                .addNewTrigger()
                .withType("ConfigChange")
                /*
                .withNewImageChangeParams()
                //.withNewImageChange()
                .withNewFrom()
                .withKind("ImageStreamTag")
                .withName(debianstretch)
                .endFrom()
                .endImageChangeParams()
                */
                .endTrigger()
                .addNewTrigger()
                .withType("ImageChange")
                .withNewImageChangeParams()
                .withAutomatic(true)
                .withContainerNames(name)
                .withNewFrom()
                .withName(image  + ":latest")
                .endFrom()
                .endImageChangeParams()
                .endTrigger()
                .endSpec()
                .build();

                /*
                .with
                .withNewGeneric()
                .withSecret("secret101")
                .endGeneric()
                .build();
                /*
                .endImageChange()
                */
                /*
                .addNewTrigger()
                .withType("ConfigChange")
                .endTrigger()
                .endSpec()
                .build();
                */
        DeploymentConfigStatus s = osClient
                .deploymentConfigs()
                .inNamespace(project)
                .createOrReplace(dc)
                .getStatus();

        ServiceStatus s2 = osClient
                .services()
                .inNamespace(project)
                .createOrReplace(srv)
                .getStatus();
        System.out.println(s.toString());
        System.out.println(s2.toString());


       //tga Controller cont = new Controller(osClient);
        //int i = controller.

        System.out.println("here10");

    }

    @Test
    public void t6() {
        createDC("amysql", "mysql");
    }

}
