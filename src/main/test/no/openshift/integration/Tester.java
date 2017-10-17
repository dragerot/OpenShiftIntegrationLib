package no.openshift.integration;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.openshift.api.model.*;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.*;


//import com.openshift.client.IOpenShiftConnection;
//import com.openshift.client.IUser;
//import com.openshift.client.OpenShiftConnectionFactory;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Tester {
    //public IOpenShiftConnection connection;
    KubernetesClient client;
    OpenShiftClient osClient;
    String project = "myproject";

    @BeforeAll
    public void setUp() {
        io.fabric8.kubernetes.api.model.Config conf;
        io.fabric8.kubernetes.client.Config conf2;
        //connection = new OpenShiftConnectionFactory().getConnection("my_user_application", "user", "password");
        conf = new io.fabric8.kubernetes.api.model.ConfigBuilder()
                .build();
        conf2 = new io.fabric8.kubernetes.client.ConfigBuilder()
                .withUsername("developer")
                .withPassword("developer")
                .build();
        client = new DefaultKubernetesClient(conf2);
        osClient = new DefaultOpenShiftClient(conf2);
        //osClient.
        System.out.println("setup done");
    }

    @AfterAll
    public void after() {
        System.out.println("after test");
        //IUser user = user = connection.getUser();
    }

    @Test
    public void myTest() {
        ServiceList myServices = osClient.services().list();
        NamespaceList myNs = client.namespaces().list();
        for (Namespace ns : myNs.getItems()) {
            System.out.println("ns " + ns.toString());
            //ns.c
        }
        for (Service item : myServices.getItems()) {
            System.out.println(item.toString());
        }
        PodList myNsServices = client.pods().inNamespace(project).list();
        for (Pod item : myNsServices.getItems()) {
            //System.out.println("iy " + item.get);
            //item.
        }
        List<ImageStream> imagestr = osClient.imageStreams().list().getItems();

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
        Map<String, String> selector = new HashMap<String,String>();
        selector.put("app", name);
        selector.put("deploymentconfig", name);
        Map<String, String> labels = new HashMap<String,String>();
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
}
