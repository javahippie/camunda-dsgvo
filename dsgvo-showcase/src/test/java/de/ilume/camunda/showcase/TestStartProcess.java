package de.ilume.camunda.showcase;

import de.ilume.camunda.showcase.constants.MessageConstants;
import de.ilume.camunda.showcase.constants.ProcessConstants;
import de.ilume.camunda.showcase.constants.TaskConstants;
import de.ilume.camunda.showcase.testutil.MapUtils;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.junit.Test;

import java.util.Map;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.processEngine;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.task;

/**
 * Test case starting an in-memory database-backed Process Engine.
 */
public class TestStartProcess extends AbstractCamundaProcessTest {

    @Test
    @Deployment(resources = "process.bpmn")
    public void testManualStartWithVariables() {
        ProcessInstance instance = processEngine()
                .getRuntimeService()
                .createProcessInstanceByKey(ProcessConstants.PROCESS_DEFINITION_KEY)
                .setVariables(buildStartRequest())
                .execute();

        assertThat(instance).isWaitingAt(TaskConstants.VALIDATE_REQUEST);
        assertThat(task()).hasCandidateGroup(ProcessConstants.CANDIDATE_GROUP_ACCOUNTING);
    }

    @Test
    @Deployment(resources = "process.bpmn")
    public void testManualStartWithMessage() {
        ProcessInstance instance = processEngine()
                .getRuntimeService()
                .startProcessInstanceByMessage(MessageConstants.MESSAGE_START_PROCESS, buildStartRequest());

        assertThat(instance).isWaitingAt(TaskConstants.VALIDATE_REQUEST);
        assertThat(task()).hasCandidateGroup(ProcessConstants.CANDIDATE_GROUP_ACCOUNTING);
    }


    private Map<String, Object> buildStartRequest() {
        return MapUtils.of(
                ProcessConstants.VARIABLE_FIRST_NAME, "Tim",
                ProcessConstants.VARIABLE_LAST_NAME, "Zöller",
                ProcessConstants.VARIABLE_STREET, "Zita-Kaiser-Str. 24",
                ProcessConstants.VARIABLE_ZIP_CODE, "79106",
                ProcessConstants.VARIABLE_CITY, "Freiburg im Breisgau",
                ProcessConstants.VARIABLE_PHONE_NUMBER, "+49 162 2 64 64 92",
                ProcessConstants.VARIABLE_EMAIL, "mail@tim-zoeller.de",
                ProcessConstants.VARIABLE_REQUEST_TEXT, "Das hier ist eine Anfrage");
    }

}
