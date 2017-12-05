package no.openshift;

import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.openshift.api.model.DeploymentConfig;
import io.fabric8.openshift.api.model.DeploymentConfigBuilder;
import io.fabric8.openshift.api.model.DeploymentConfigStatus;
import io.fabric8.openshift.api.model.RollingDeploymentStrategyParams;

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
public class NginxDeployment {
    public static DeploymentConfig createDeplymentConfig(String nameSpace, String deploymentConfigName, String app) {
        String VERSJON_API ="v1";
        String PROSJEKTNAVN = nameSpace;
        //String DEPLOYMENT_CONFIG_NAME = "nginx-deployment";
        String DEPLOYMENT_CONFIG_NAME = deploymentConfigName;
        String IMAGE_NAME = "nginx:1.13.7";
        //String CONTAINER_NAME = "nginx";
        String CONTAINER_NAME =app;
        int PORT =8083;

        RollingDeploymentStrategyParams rollingDeploymentStrategyParams = new RollingDeploymentStrategyParams();
        rollingDeploymentStrategyParams.setIntervalSeconds(1l);
        rollingDeploymentStrategyParams.setMaxSurge(new IntOrString("25%"));
        rollingDeploymentStrategyParams.setMaxUnavailable(new IntOrString("25%"));
        rollingDeploymentStrategyParams.setTimeoutSeconds(600l);
        rollingDeploymentStrategyParams.setUpdatePeriodSeconds(1l);

        DeploymentConfigStatus deploymentConfigStatus = new DeploymentConfigStatus();
        deploymentConfigStatus.setAvailableReplicas(Integer.getInteger("1"));

        DeploymentConfig deploymentConfig = new DeploymentConfigBuilder()
                .withApiVersion(VERSJON_API)
                .withNewMetadata()
                .addToLabels("app", CONTAINER_NAME)
                .withName(DEPLOYMENT_CONFIG_NAME)
                .withNamespace(PROSJEKTNAVN)
                .endMetadata()
                .withNewSpec()
                .withReplicas(1)
                .addToSelector("app", CONTAINER_NAME)
                .withNewStrategy()
                .withActiveDeadlineSeconds(21600l)
                .withRollingParams(rollingDeploymentStrategyParams)
                .withType("Rolling")
                .endStrategy()
                .withNewTemplate()
                .withNewMetadata()
                .addToLabels("app", CONTAINER_NAME)
                .endMetadata()
                .withNewSpec()
                .addNewContainer()
                .withImage(IMAGE_NAME)
                .withImagePullPolicy("IfNotPresent")
                .withName(CONTAINER_NAME)
                .addNewPort()
                .withContainerPort(PORT)
                .withProtocol("TCP")
                .endPort()
                .withTerminationMessagePath("/dev/termination-log")
                .withTerminationMessagePolicy("File")
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
}
