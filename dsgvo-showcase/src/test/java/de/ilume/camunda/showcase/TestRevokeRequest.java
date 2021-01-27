package de.ilume.camunda.showcase;

import de.ilume.camunda.showcase.constants.MessageConstants;
import de.ilume.camunda.showcase.constants.ProcessConstants;
import de.ilume.camunda.showcase.constants.TaskConstants;
import de.ilume.camunda.showcase.delegates.LoggerDelegate;
import de.ilume.camunda.showcase.testutil.FunctionalInterfaceDelegate;
import de.ilume.camunda.showcase.testutil.MapUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.junit.Before;
import org.junit.Test;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.processEngine;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.task;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.taskService;

/**
 * Test case starting an in-memory database-backed Process Engine.
 */
public class TestRevokeRequest extends AbstractCamundaProcessTest {

    private final static String BUSINESS_KEY_01 = "abc1234";
    private final static String BUSINESS_KEY_02 = "abc1235";

    private void registerQuickMock(String name, FunctionalInterfaceDelegate md) {
        Mocks.register(name, md);
    }

    @Test
    @Deployment(resources = "process.bpmn")
    public void testMessageCorrelationWithTwoInstances() {
        ProcessInstance instance1 = processEngine()
                .getRuntimeService()
                .startProcessInstanceByKey(ProcessConstants.PROCESS_DEFINITION_KEY, BUSINESS_KEY_01);

        ProcessInstance instance2 = processEngine()
                .getRuntimeService()
                .startProcessInstanceByKey(ProcessConstants.PROCESS_DEFINITION_KEY, BUSINESS_KEY_02);

        registerQuickMock("verifyIdentityOnCancel",  execution -> execution.setVariable("identityConfirmed", true));

        assertThat(instance1).isWaitingAt(TaskConstants.VALIDATE_REQUEST);
        assertThat(instance2).isWaitingAt(TaskConstants.VALIDATE_REQUEST);

        processEngine().getRuntimeService().correlateMessage(MessageConstants.MESSAGE_REVOKE_PROCESS, BUSINESS_KEY_01);
        assertThat(instance1).isEnded();
        assertThat(instance2).isWaitingAt(TaskConstants.VALIDATE_REQUEST);

        registerQuickMock("verifyIdentityOnCancel",  execution -> execution.setVariable("identityConfirmed", false));

        processEngine().getRuntimeService().correlateMessage(MessageConstants.MESSAGE_REVOKE_PROCESS, BUSINESS_KEY_02);
        assertThat(instance2).isWaitingAt(TaskConstants.VALIDATE_REVOKER_IDENTITY, TaskConstants.VALIDATE_REQUEST);
        taskService().complete(task(TaskConstants.VALIDATE_REVOKER_IDENTITY, instance2).getId(), MapUtils.of(ProcessConstants.VARIABLE_IDENTITY_CONFIRMED, false));
        assertThat(instance2).isWaitingAt(TaskConstants.VALIDATE_REQUEST).isNotEnded();
    }


}
