package no.openshift.integration;

import io.fabric8.openshift.client.OpenShiftClient;
import no.openeshift.integration.OpenShiftConfig;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@Import(OpenShiftConfig.class)
public class TestEtt {
    @Autowired
    OpenShiftClient openShiftClient;

    public void hallo(){
        Assert.assertNotNull(openShiftClient);
    }
}
