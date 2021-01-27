package de.ilume.camunda.showcase.delegates;

import de.ilume.camunda.showcase.constants.ProcessConstants;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

import static org.camunda.spin.Spin.JSON;

@Named("fetchDataHr")
public class FetchDataHrDelegate  implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String lastName = (String) execution.getVariable(ProcessConstants.VARIABLE_LAST_NAME);
        String firstName = (String) execution.getVariable(ProcessConstants.VARIABLE_FIRST_NAME);

        List<String> reportData = new ArrayList<>();
        reportData.add(firstName + " " + lastName + ": Application received on 30.06.2018");
        reportData.add(firstName + " " + lastName + ": Application denied on 31.07.2018");

        execution.setVariable(ProcessConstants.VARIABLE_HR_REPORT_DATA, JSON(reportData).toString());
    }

}
