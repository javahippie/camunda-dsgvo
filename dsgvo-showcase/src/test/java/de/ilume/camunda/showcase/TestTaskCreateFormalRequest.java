package de.ilume.camunda.showcase;

import de.ilume.camunda.showcase.constants.ProcessConstants;
import de.ilume.camunda.showcase.constants.TaskConstants;
import de.ilume.camunda.showcase.delegates.LoggerDelegate;
import org.camunda.bpm.engine.impl.persistence.entity.MessageEntity;
import org.camunda.bpm.engine.runtime.Job;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.junit.Before;
import org.junit.Test;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.jobQuery;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.managementService;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.processEngine;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.task;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.taskService;

/**
 * Test case starting an in-memory database-backed Process Engine.
 */
public class TestTaskCreateFormalRequest extends AbstractCamundaProcessTest {

    @Before
    public void registerMocks() {
        Mocks.register("logger", new LoggerDelegate());
    }

    @Test
    @Deployment(resources = "process.bpmn")
    public void testTaskValidateRequestPositive() {
        ProcessInstance processInstance = startProcessInstanceFromCreateFormalRequest();
        assertThat(processInstance).isWaitingAt(TaskConstants.CREATE_FORMAL_REQUEST);
        assertThat(task()).hasCandidateGroup(ProcessConstants.CANDIDATE_GROUP_ACCOUNTING);
        taskService().complete(task().getId());

        assertThat(processInstance).isWaitingAt(TaskConstants.WRITE_INITAL_PROTOCOL);
        executeCurrentAsyncTask();
        assertThat(processInstance).isWaitingAt(TaskConstants.WRITE_CONFIRMATION_EMAIL);
        executeCurrentAsyncTask();
        assertThat(processInstance).isWaitingAt(TaskConstants.VALIDATE_IDENTITY);
    }

    private ProcessInstance startProcessInstanceFromCreateFormalRequest() {
        return processEngine()
                .getRuntimeService()
                .createProcessInstanceByKey(ProcessConstants.PROCESS_DEFINITION_KEY)
                .startBeforeActivity(TaskConstants.CREATE_FORMAL_REQUEST)
                .execute();
    }

}
