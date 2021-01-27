package de.ilume.camunda.showcase.delegates;

import de.ilume.camunda.showcase.constants.ProcessConstants;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;

@Named("fetchDataSales")
public class FetchDataSalesDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        execution.setVariable(ProcessConstants.VARIABLE_HR_REPORT_DATA, null);
    }
    
}
