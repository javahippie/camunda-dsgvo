package de.ilume.camunda.showcase.testutil;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

@FunctionalInterface
public interface FunctionalInterfaceDelegate extends JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception;
}
