package de.ilume.camunda.showcase.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;
import java.util.Map;

@Named("verifyIdentityOnCancel")
public class VerifyIdentityOnCancelDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        Map<String, Object> variables = execution.getVariables();

        boolean valuesMatch = variables.keySet()
                .stream()
                .filter(key -> key.endsWith("Cancel"))
                .allMatch(key -> {
                    Object cancelValue = variables.get(key);
                    Object initialValue = variables.get(key.replaceAll("Cancel", ""));
                    return cancelValue.equals(initialValue);
                });

        execution.setVariable("identityConfirmed", valuesMatch);

    }

}
