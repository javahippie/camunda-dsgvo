package de.ilume.camunda.showcase;

import de.ilume.camunda.showcase.constants.ProcessConstants;
import de.ilume.camunda.showcase.constants.TaskConstants;
import de.ilume.camunda.showcase.testutil.MapUtils;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.junit.Test;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.processEngine;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.task;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.taskService;

/**
 * Test case starting an in-memory database-backed Process Engine.
 */
public class TestTaskValidateRequest extends AbstractCamundaProcessTest {

    @Test
    @Deployment(resources = "process.bpmn")
    public void testTaskValidateRequestPositive() {
        ProcessInstance processInstance = startProcessInstanceFromValidateRequest();
        assertThat(processInstance).isWaitingAt(TaskConstants.VALIDATE_REQUEST);
        assertThat(task()).hasCandidateGroup(ProcessConstants.CANDIDATE_GROUP_ACCOUNTING);
        taskService().complete(task().getId(), MapUtils.of(ProcessConstants.VARIABLE_IS_DSGVO_REQUEST, true));

        assertThat(processInstance).isWaitingAt(TaskConstants.CREATE_FORMAL_REQUEST);
        assertThat(task()).hasCandidateGroup(ProcessConstants.CANDIDATE_GROUP_ACCOUNTING);
    }


    @Test
    @Deployment(resources = "process.bpmn")
    public void testTaskValidateRequestNegative() {
        ProcessInstance processInstance = startProcessInstanceFromValidateRequest();
        assertThat(processInstance).isWaitingAt(TaskConstants.VALIDATE_REQUEST);
        assertThat(task()).hasCandidateGroup(ProcessConstants.CANDIDATE_GROUP_ACCOUNTING);
        taskService().complete(task().getId(), MapUtils.of(ProcessConstants.VARIABLE_IS_DSGVO_REQUEST, false));

        assertThat(processInstance).isWaitingAt(TaskConstants.CREATE_FORMAL_REJECTION);
        assertThat(task()).hasCandidateGroup(ProcessConstants.CANDIDATE_GROUP_ACCOUNTING);

        taskService().complete(task().getId());
        assertThat(processInstance).isEnded();
    }

    private ProcessInstance startProcessInstanceFromValidateRequest() {
        return processEngine()
                .getRuntimeService()
                .createProcessInstanceByKey(ProcessConstants.PROCESS_DEFINITION_KEY)
                .startBeforeActivity(TaskConstants.VALIDATE_REQUEST)
                .execute();
    }

}
