package de.ilume.camunda.showcase;

import de.ilume.camunda.showcase.constants.ProcessConstants;
import de.ilume.camunda.showcase.delegates.LoggerDelegate;
import de.ilume.camunda.showcase.testutil.MapUtils;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.camunda.spin.Spin;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static de.ilume.camunda.showcase.constants.TaskConstants.CREATE_DATA_REPORT;
import static de.ilume.camunda.showcase.constants.TaskConstants.CREATE_MANUAL_ANSWER;
import static de.ilume.camunda.showcase.constants.TaskConstants.MANUAL_RESULT_CHECK;
import static de.ilume.camunda.showcase.constants.TaskConstants.SEND_DATA_REPORT;
import static de.ilume.camunda.showcase.constants.TaskConstants.SEND_NEGATIVE_REPORT;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.processEngine;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.task;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.taskService;

/**
 * Test case starting an in-memory database-backed Process Engine.
 */
public class TestApproveFoundResults extends AbstractCamundaProcessTest {

    @Before
    public void registerMocks() {
        Mocks.register("logger", new LoggerDelegate());
    }

    @Test
    @Deployment(resources = "process.bpmn")
    public void testManualResultCheckPositiveAndDataFound() {
        ProcessInstance instance = startProcessInstanceFromCreateFormalRequest();
        assertThat(instance).isWaitingAt(MANUAL_RESULT_CHECK);
        taskService().complete(task().getId(), MapUtils.of(
                ProcessConstants.VARIABLE_APPROVAL_GIVEN, true,
                ProcessConstants.VARIABLE_HR_REPORT_DATA, "['a', 'b']",
                ProcessConstants.VARIABLE_SALES_REPORT_DATA, null,
                ProcessConstants.VARIABLE_ACCOUNTING_REPORT_DATA, null));
        assertThat(instance).hasPassed(CREATE_DATA_REPORT, SEND_DATA_REPORT).isEnded();
    }

    @Test
    @Deployment(resources = "process.bpmn")
    public void testManualResultCheckPositiveAndNoDataFound() {
        ProcessInstance instance = startProcessInstanceFromCreateFormalRequest();
        assertThat(instance).isWaitingAt(MANUAL_RESULT_CHECK);
        taskService().complete(task().getId(), MapUtils.of(
                ProcessConstants.VARIABLE_APPROVAL_GIVEN, true,
                ProcessConstants.VARIABLE_HR_REPORT_DATA, null,
                ProcessConstants.VARIABLE_SALES_REPORT_DATA, null,
                ProcessConstants.VARIABLE_ACCOUNTING_REPORT_DATA, null));
        assertThat(instance).hasPassed(SEND_NEGATIVE_REPORT).isEnded();
    }

    @Test
    @Deployment(resources = "process.bpmn")
    public void testManualResultCheckNegative() {
        ProcessInstance instance = startProcessInstanceFromCreateFormalRequest();
        assertThat(instance).isWaitingAt(MANUAL_RESULT_CHECK);
        taskService().complete(task().getId(), MapUtils.of(ProcessConstants.VARIABLE_APPROVAL_GIVEN, false));
        assertThat(instance).isWaitingAt(CREATE_MANUAL_ANSWER);
        taskService().complete(task().getId());
        assertThat(instance).isEnded();

    }

    private ProcessInstance startProcessInstanceFromCreateFormalRequest() {
        return processEngine()
                .getRuntimeService()
                .createProcessInstanceByKey(ProcessConstants.PROCESS_DEFINITION_KEY)
                .startBeforeActivity(MANUAL_RESULT_CHECK)
                .execute();
    }

}
