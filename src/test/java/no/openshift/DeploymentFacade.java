package no.openshift;

import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.openshift.api.model.*;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;
import org.omg.IOP.ServiceContextHelper;

import java.util.HashMap;
import java.util.Map;

/*
apiVersion: v1
kind: DeploymentConfig
metadata:
  creationTimestamp: '2017-12-05T12:43:48Z'
  generation: 2
  labels:
    app: nginx
  name: nginx-deployment
  namespace: etprosjekt
spec:
  replicas: 1
  selector:
    app: nginx
  strategy:
    activeDeadlineSeconds: 21600
    resources: {}
    rollingParams:
      intervalSeconds: 1
      maxSurge: 25%
      maxUnavailable: 25%
      timeoutSeconds: 600
      updatePeriodSeconds: 1
    type: Rolling
  template:
    metadata:
      creationTimestamp: null
      labels:
        app: nginx
    spec:
      containers:
        - image: 'nginx:1.13.7'
          imagePullPolicy: IfNotPresent
          name: nginx
          ports:
            - containerPort: 8083
              protocol: TCP
            - containerPort: 8084
              protocol: TCP
          resources: {}
          terminationMessagePath: /dev/termination-log
          terminationMessagePolicy: File
      dnsPolicy: ClusterFirst
      restartPolicy: Always
      schedulerName: default-scheduler
      securityContext: {}
      terminationGracePeriodSeconds: 30
  test: false
  triggers:
    - type: ConfigChange
status:
  availableReplicas: 1
 */
public class DeploymentFacade {
    private static final String VERSJON_API ="v1";
    private static final String URL = "https://openshift:8443";

    public static OpenShiftClient getOpenShiftClient(String user, String passord, String oauthToken) {
        Config config = new ConfigBuilder().withMasterUrl(URL).build();
        config.setTrustCerts(true);
        if (oauthToken.trim().length() > 0) {
            config.setOauthToken(oauthToken);
        } else {
            config.setUsername(user);
            config.setPassword(passord);

        }
        return new DefaultOpenShiftClient(config);

    }

    public static DeploymentConfig createDeplymentConfig(String nameSpace, String deploymentConfigName, String imagename, String app, int port, String apiVersion) {
        RollingDeploymentStrategyParams rollingDeploymentStrategyParams = new RollingDeploymentStrategyParams();
        rollingDeploymentStrategyParams.setIntervalSeconds(1l);
        rollingDeploymentStrategyParams.setMaxSurge(new IntOrString("25%"));
        rollingDeploymentStrategyParams.setMaxUnavailable(new IntOrString("25%"));
        rollingDeploymentStrategyParams.setTimeoutSeconds(600l);
        rollingDeploymentStrategyParams.setUpdatePeriodSeconds(1l);

        DeploymentConfigStatus deploymentConfigStatus = new DeploymentConfigStatus();
        deploymentConfigStatus.setAvailableReplicas(Integer.getInteger("1"));

        DeploymentConfig deploymentConfig = new DeploymentConfigBuilder()
                .withApiVersion(apiVersion)
                .withNewMetadata()
                .addToLabels("app", app)
                .withName(deploymentConfigName)
                .withNamespace(nameSpace)
                .endMetadata()
                .withNewSpec()
                .withReplicas(1)
                .addToSelector("app", app)
                .withNewStrategy()
                .withActiveDeadlineSeconds(21600l)
                .withRollingParams(rollingDeploymentStrategyParams)
                .withType("Rolling")
                .endStrategy()
                .withNewTemplate()
                .withNewMetadata()
                .addToLabels("app", app)
                .endMetadata()
                .withNewSpec()
                .addNewContainer()
                .withImage(imagename)
                .withImagePullPolicy("IfNotPresent")
                .withName(app)
                .addNewPort()
                .withContainerPort(port)
                .withProtocol("TCP")
                .endPort()
//                .withTerminationMessagePath("/dev/termination-log")
//                .withTerminationMessagePolicy("File")
                .endContainer()
                .withDnsPolicy("ClusterFirst")
                .withRestartPolicy("Always")
                .withSchedulerName("default-scheduler")
                .withTerminationGracePeriodSeconds(30l)
                .endSpec()
                .endTemplate()
                .withTest(false)
                .withTriggers()
                .addNewTrigger()
                .withType("ConfigChange")
                .endTrigger()
                .endSpec()
                .withStatus(deploymentConfigStatus)
                .build();

        return deploymentConfig;
    }

    public static Service createService(String nameSpace, int port, int exPort,String protocol,String app,String apiVersion){
        String portFormatted = String.format("%d-%s",port,protocol);
        return new ServiceBuilder()
                .withApiVersion(apiVersion)
                .withNewMetadata()
                .withName(app+"-service")
                .withNamespace(nameSpace)
                .endMetadata()
                .withNewSpec()
                .addToSelector("app", app)
                .addNewPort()
                .withName(portFormatted)
                .withProtocol("TCP")
                .withPort(port)
                .withNewTargetPort(exPort)
                .endPort()
                .endSpec()
                .build();
    }

    public static Route createRoute(String nameSpace,String app, String path, int targetPort, String protocol,String apiVersion){
        String targetPortformatted = String.format("%d-%s",targetPort,protocol);
        return new RouteBuilder()
                .withApiVersion(apiVersion)
                .withNewMetadata()
                .withName(app+"-route")
                .withNamespace(nameSpace)
                .addToLabels("app",app)
                .endMetadata()
                .withNewSpec()
                //.withHost(app+"-vegvesen.no")
                .withPath(path)
                .withNewPort()
                 .withNewTargetPort(targetPortformatted)
                .endPort()
                .withNewTo()
                .withKind("Service")
                .withName(app+"-service")
                .endTo()
//                .withNewTls()
//                    .withTermination("passthrough")
//                .endTls()
                .endSpec().build();
    }
}
