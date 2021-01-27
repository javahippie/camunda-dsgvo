package de.ilume.camunda.showcase.delegates;

import de.ilume.camunda.showcase.constants.ProcessConstants;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import static org.camunda.spin.Spin.JSON;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named("fetchDataAccounting")
public class FetchDataAccountingDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        String lastName = (String) execution.getVariable(ProcessConstants.VARIABLE_LAST_NAME);
        String firstName = (String) execution.getVariable(ProcessConstants.VARIABLE_FIRST_NAME);

        List<String> reportData = new ArrayList<>();
        reportData.add(firstName + " " + lastName + ": Invoice created on 12.01.2017");
        reportData.add(firstName + " " + lastName + ": Invoice created on 22.03.2017");

        execution.setVariable(ProcessConstants.VARIABLE_ACCOUNTING_REPORT_DATA, JSON(reportData).toString());
    }
    
}
