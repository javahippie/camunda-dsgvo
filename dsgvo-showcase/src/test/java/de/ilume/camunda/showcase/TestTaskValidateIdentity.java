package de.ilume.camunda.showcase;

import de.ilume.camunda.showcase.constants.ProcessConstants;

import de.ilume.camunda.showcase.delegates.LoggerDelegate;
import de.ilume.camunda.showcase.testutil.MapUtils;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.junit.Before;
import org.junit.Test;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.processEngine;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.task;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.taskService;
import static de.ilume.camunda.showcase.constants.TaskConstants.*;

/**
 * Test case starting an in-memory database-backed Process Engine.
 */
public class TestTaskValidateIdentity extends AbstractCamundaProcessTest {

    @Before
    public void registerMocks() {
        Mocks.register("logger", new LoggerDelegate());
    }

    @Test
    @Deployment(resources = "process.bpmn")
    public void testTaskValidateIdentityPositive() {
        ProcessInstance processInstance = startProcessInstanceFromCreateFormalRequest();
        assertThat(processInstance).isWaitingAt(VALIDATE_IDENTITY);
        assertThat(task()).hasCandidateGroup(ProcessConstants.CANDIDATE_GROUP_ACCOUNTING);
        taskService().complete(task().getId(), MapUtils.of(ProcessConstants.VARIABLE_IDENTITY_CONFIRMED, true));
        assertThat(processInstance).hasPassed(GET_ACCOUNTING_DATA, GET_HR_DATA, GET_SALES_DATA, PROTOCOL_RESULT);
        assertThat(processInstance).isWaitingAt(MANUAL_RESULT_CHECK);
    }

    @Test
    @Deployment(resources = "process.bpmn")
    public void testTaskValidateIdentityNegative() {
        ProcessInstance processInstance = startProcessInstanceFromCreateFormalRequest();
        assertThat(processInstance).isWaitingAt(VALIDATE_IDENTITY);
        assertThat(task()).hasCandidateGroup(ProcessConstants.CANDIDATE_GROUP_ACCOUNTING);
        taskService().complete(task().getId(), MapUtils.of(ProcessConstants.VARIABLE_IDENTITY_CONFIRMED, false));
        assertThat(processInstance).hasNotPassed(GET_ACCOUNTING_DATA, GET_HR_DATA, GET_SALES_DATA, PROTOCOL_RESULT);
        assertThat(processInstance).isWaitingAt(COMMUNICATE_REJECTION);
        taskService().complete(task().getId());
        assertThat(processInstance).isEnded();
    }

    private ProcessInstance startProcessInstanceFromCreateFormalRequest() {
        return processEngine()
                .getRuntimeService()
                .createProcessInstanceByKey(ProcessConstants.PROCESS_DEFINITION_KEY)
                .startBeforeActivity(VALIDATE_IDENTITY)
                .setVariables(MapUtils.of(
                        ProcessConstants.VARIABLE_SEARCH_IN_ACCOUNTING, true,
                        ProcessConstants.VARIABLE_SEARCH_IN_HR, true,
                        ProcessConstants.VARIABLE_SEARCH_IN_SALES, true))
                .execute();
    }

}
