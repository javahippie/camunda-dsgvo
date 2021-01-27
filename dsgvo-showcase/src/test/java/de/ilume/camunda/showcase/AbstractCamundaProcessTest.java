package de.ilume.camunda.showcase;

import org.apache.ibatis.logging.LogFactory;
import org.camunda.bpm.engine.impl.persistence.entity.MessageEntity;
import org.camunda.bpm.engine.runtime.Job;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.extension.process_test_coverage.junit.rules.TestCoverageProcessEngineRuleBuilder;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.init;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.jobQuery;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.managementService;

public class AbstractCamundaProcessTest {


    static {
        LogFactory.useSlf4jLogging(); // MyBatis
    }

    @ClassRule
    @Rule
    public static ProcessEngineRule rule = TestCoverageProcessEngineRuleBuilder.create().build();

    @Before
    public void setup() {
        init(rule.getProcessEngine());
    }

    protected void executeCurrentAsyncTask() {
        jobQuery().active().list()
                .stream()
                .filter(job -> job instanceof MessageEntity)
                .map(Job::getId)
                .forEach(managementService()::executeJob);
    }
}
