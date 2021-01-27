package de.ilume.camunda.showcase;

import de.ilume.camunda.showcase.constants.ProcessConstants;
import de.ilume.camunda.showcase.constants.TaskConstants;
import de.ilume.camunda.showcase.delegates.LoggerDelegate;
import org.camunda.bpm.engine.runtime.Job;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.processEngine;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.jobQuery;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.managementService;

public class TestDeadlineRanOut extends AbstractCamundaProcessTest {

    @Before
    public void registerMocks() {
        Mocks.register("logger", new LoggerDelegate());
    }

    @Test
    @Deployment(resources = "process.bpmn")
    public void testTimersBeforeDeadline() {
        ProcessInstance instance = processEngine()
                .getRuntimeService()
                .startProcessInstanceByKey(ProcessConstants.PROCESS_DEFINITION_KEY);

        assertThat(instance).isWaitingAt(TaskConstants.VALIDATE_REQUEST);

        executeNextTimerEvent();

        assertThat(instance).hasPassed(ProcessConstants.BOUNDARY_EVENT_FIVE_DAYS_BEFORE,
                TaskConstants.NOTIFY_FIVE_DAYS_BEFORE_DEADLINE,
                ProcessConstants.END_EVENT_FIVE_DAY_NOTIFICATION)
                .isNotEnded();

        executeNextTimerEvent();

        assertThat(instance).hasPassed(ProcessConstants.BOUNDARY_EVENT_AT_DEADLINE,
                TaskConstants.NOTIFY_AT_DEADLINE,
                ProcessConstants.END_EVENT_FINAL_NOTIFICATION)
                .isNotEnded();
    }

    private void executeNextTimerEvent() {
        Optional<String> nextTimer = jobQuery()
                .active()
                .orderByJobDuedate()
                .asc()
                .list()
                .stream()
                .map(Job::getId)
                .findFirst();

        managementService().executeJob(nextTimer.get());
    }

}
