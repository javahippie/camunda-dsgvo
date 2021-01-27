package de.ilume.camunda.showcase;

import de.ilume.camunda.showcase.constants.ProcessConstants;
import de.ilume.camunda.showcase.constants.TaskConstants;
import de.ilume.camunda.showcase.delegates.FetchDataAccountingDelegate;
import de.ilume.camunda.showcase.delegates.FetchDataHrDelegate;
import de.ilume.camunda.showcase.delegates.FetchDataSalesDelegate;
import de.ilume.camunda.showcase.delegates.LoggerDelegate;
import de.ilume.camunda.showcase.testutil.MapUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.processEngine;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.task;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.taskService;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Test case starting an in-memory database-backed Process Engine.
 */
@RunWith(MockitoJUnitRunner.class)
public class TestDataReportGeneration extends AbstractCamundaProcessTest {

    @Mock
    private FetchDataHrDelegate hrDelegate;

    @Mock
    private FetchDataAccountingDelegate accountingDelegate;

    @Mock
    FetchDataSalesDelegate salesDelegate;

    @Before
    public void registerMocks() throws Exception {

        doNothing().when(hrDelegate).execute(any(DelegateExecution.class));
        doNothing().when(salesDelegate).execute(any(DelegateExecution.class));
        doNothing().when(accountingDelegate).execute(any(DelegateExecution.class));

        Mocks.register("fetchDataHr", hrDelegate);
        Mocks.register("fetchDataSales", salesDelegate);
        Mocks.register("fetchDataAccounting", accountingDelegate);
        Mocks.register("logger", new LoggerDelegate());
    }

    @Test
    @Deployment(resources = "process.bpmn")
    public void testOnlyHr() throws Exception {
        ProcessInstance instance = startProcessInstanceFromCreateFormalRequest(MapUtils.of(
                ProcessConstants.VARIABLE_SEARCH_IN_ACCOUNTING, false,
                ProcessConstants.VARIABLE_SEARCH_IN_HR, true,
                ProcessConstants.VARIABLE_SEARCH_IN_SALES, false));

        assertThat(instance).isWaitingAt(TaskConstants.WRITE_INITAL_PROTOCOL);
        super.executeCurrentAsyncTask();
        super.executeCurrentAsyncTask();
        taskService().complete(task().getId(), MapUtils.of(ProcessConstants.VARIABLE_IDENTITY_CONFIRMED, true));

        verify(hrDelegate, times(1)).execute(any(DelegateExecution.class));
        verifyZeroInteractions(salesDelegate);
        verifyZeroInteractions(accountingDelegate);

        assertThat(instance).hasPassed(TaskConstants.GET_HR_DATA).hasNotPassed(TaskConstants.GET_SALES_DATA, TaskConstants.GET_ACCOUNTING_DATA);
    }

    @Test
    @Deployment(resources = "process.bpmn")
    public void testOnlySales() throws Exception {
        ProcessInstance instance = startProcessInstanceFromCreateFormalRequest(MapUtils.of(
                ProcessConstants.VARIABLE_SEARCH_IN_ACCOUNTING, false,
                ProcessConstants.VARIABLE_SEARCH_IN_HR, false,
                ProcessConstants.VARIABLE_SEARCH_IN_SALES, true));

        assertThat(instance).isWaitingAt(TaskConstants.WRITE_INITAL_PROTOCOL);
        super.executeCurrentAsyncTask();
        super.executeCurrentAsyncTask();
        taskService().complete(task().getId(), MapUtils.of(ProcessConstants.VARIABLE_IDENTITY_CONFIRMED, true));

        verify(salesDelegate, times(1)).execute(any(DelegateExecution.class));
        verifyZeroInteractions(hrDelegate);
        verifyZeroInteractions(accountingDelegate);

        assertThat(instance).hasPassed(TaskConstants.GET_SALES_DATA).hasNotPassed(TaskConstants.GET_HR_DATA, TaskConstants.GET_ACCOUNTING_DATA);
    }

    @Test
    @Deployment(resources = "process.bpmn")
    public void testOnlyAccounting() throws Exception {
        ProcessInstance instance = startProcessInstanceFromCreateFormalRequest(MapUtils.of(
                ProcessConstants.VARIABLE_SEARCH_IN_ACCOUNTING, true,
                ProcessConstants.VARIABLE_SEARCH_IN_HR, false,
                ProcessConstants.VARIABLE_SEARCH_IN_SALES, false));

        assertThat(instance).isWaitingAt(TaskConstants.WRITE_INITAL_PROTOCOL);
        super.executeCurrentAsyncTask();
        super.executeCurrentAsyncTask();
        taskService().complete(task().getId(), MapUtils.of(ProcessConstants.VARIABLE_IDENTITY_CONFIRMED, true));

        verify(accountingDelegate, times(1)).execute(any(DelegateExecution.class));
        verifyZeroInteractions(hrDelegate);
        verifyZeroInteractions(salesDelegate);

        assertThat(instance).hasPassed(TaskConstants.GET_ACCOUNTING_DATA).hasNotPassed(TaskConstants.GET_HR_DATA, TaskConstants.GET_SALES_DATA);
    }

    private ProcessInstance startProcessInstanceFromCreateFormalRequest(Map<String, Object> variables) {
        return processEngine()
                .getRuntimeService()
                .createProcessInstanceByKey(ProcessConstants.PROCESS_DEFINITION_KEY)
                .startAfterActivity(TaskConstants.CREATE_FORMAL_REQUEST)
                .setVariables(variables)
                .execute();
    }

}
