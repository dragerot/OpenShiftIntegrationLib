package no.openshift;

import io.fabric8.openshift.client.OpenShiftClient;
import org.junit.Test;

public class NginxDeploymentExampl {
    @Test
    public void deployment_nginx_Test() {
        String PROSJEKTNAVN = "fjernmegproject";
        String DEPLOYMENTCONFIG_NAME= "nginx-deployment";
        String IMAGE_NAME = "nginx:1.13.7";
        String APP_NAME = "nginx";
        String PATH ="/";
        String API_VERSION="v1";
        int PORT =8083;
        int TARGET_PORT =8085;
        String TARGET_PORT_PROTOCOL="tcp";




        try {
            OpenShiftClient osClient = DeploymentFacade.getOpenShiftClient("developer", "developer", "");
            System.out.println("****CREATE DEPLOYMENTCONFIG**************************");
            osClient.deploymentConfigs().create(DeploymentFacade.createDeplymentConfig(PROSJEKTNAVN,DEPLOYMENTCONFIG_NAME,IMAGE_NAME,APP_NAME,PORT,API_VERSION));
            System.out.println("****CREATE SERVICE**************************");
            osClient.services().create(DeploymentFacade.createService(PROSJEKTNAVN,PORT,TARGET_PORT,TARGET_PORT_PROTOCOL,APP_NAME,API_VERSION));
            System.out.println("****CREATE ROUTE**************************");
            //https://docs.openshift.org/latest/dev_guide/routes.html
            osClient.routes().create(DeploymentFacade.createRoute(PROSJEKTNAVN,APP_NAME,PATH,TARGET_PORT,TARGET_PORT_PROTOCOL,API_VERSION));
        } catch (Exception e) {
            System.out.println("*****deployment_nginx_Test***************ERROR" + e.getMessage());
        }
    }
}
