package no.openshift;

import io.fabric8.openshift.client.OpenShiftClient;
import org.junit.Test;

public class SpringBootDeploymentExampl {

    @Test
    public void deployment_springboot_Test() {
        String PROSJEKTNAVN_fjernmegproject = "fjernmegproject";
        String PROSJEKTNAVN_etprosjekt = "etprosjekt";
        String PROSJEKTNAVN__SELECTED ="";
        String DEPLOYMENTCONFIG_NAME= "springboot-deployment";
        String IMAGE_NAME = "toregard/jerseydemo1:v1";
        String APP_NAME = "springboot";
        String PATH ="/";
        String API_VERSION="v1";
        int PORT =8080;
        int TARGET_PORT =8080;
        String TARGET_PORT_PROTOCOL="tcp";

        PROSJEKTNAVN__SELECTED = PROSJEKTNAVN_fjernmegproject;
        try {
            OpenShiftClient osClient = DeploymentFacade.getOpenShiftClient("developer", "developer", "");

            System.out.println("****CREATE DEPLOYMENTCONFIG**************************");
            osClient.deploymentConfigs()
                    .inNamespace(PROSJEKTNAVN__SELECTED)
                    .create(DeploymentFacade.createDeplymentConfig(PROSJEKTNAVN__SELECTED,DEPLOYMENTCONFIG_NAME,IMAGE_NAME,APP_NAME,PORT,API_VERSION));

            System.out.println("****CREATE SERVICE**************************");
            osClient.services()
                    .inNamespace(PROSJEKTNAVN__SELECTED)
                    .create(DeploymentFacade.createService(PROSJEKTNAVN__SELECTED,PORT,TARGET_PORT,TARGET_PORT_PROTOCOL,APP_NAME,API_VERSION));

            System.out.println("****CREATE ROUTE**************************");
            //https://docs.openshift.org/latest/dev_guide/routes.html
            osClient.routes()
                    .inNamespace(PROSJEKTNAVN__SELECTED)
                    .create(DeploymentFacade.createRoute(PROSJEKTNAVN__SELECTED,APP_NAME,PATH,TARGET_PORT,TARGET_PORT_PROTOCOL,API_VERSION));
        } catch (Exception e) {
            System.out.println("*****deployment_springboot_Test***************ERROR" + e.getMessage());
        }
    }
}
