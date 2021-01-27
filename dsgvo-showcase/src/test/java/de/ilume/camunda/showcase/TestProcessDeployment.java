package de.ilume.camunda.showcase;

import org.camunda.bpm.engine.test.Deployment;
import org.junit.Test;

/**
 * Test case starting an in-memory database-backed Process Engine.
 */
public class TestProcessDeployment extends AbstractCamundaProcessTest{

    /**
     * Just tests if the process definition is deployable.
     */
    @Test
    @Deployment(resources = "process.bpmn")
    public void testParsingAndDeployment() {
        // nothing is done here, as we just want to check for exceptions during deployment
    }

}
