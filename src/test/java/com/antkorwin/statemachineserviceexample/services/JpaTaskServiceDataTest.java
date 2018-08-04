package com.antkorwin.statemachineserviceexample.services;

import com.antkorwin.junit5integrationtestutils.test.runners.EnableRiderTests;
import com.antkorwin.statemachineserviceexample.TestDataHelper;
import com.antkorwin.statemachineserviceexample.models.States;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.antkorwin.statemachineserviceexample.TestDataHelper.TASK_ID;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 23.07.2018.
 *
 * @author Korovin Anatoliy
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@EnableRiderTests
public class JpaTaskServiceDataTest {

    @Autowired
    private TaskService taskService;

    @Test
    void testDI() {
        assertThat(taskService).isNotNull();
    }

    @Test
    @DataSet(cleanAfter = true, cleanBefore = true)
    @ExpectedDataSet("datasets/expected.json")
    void testCreate() {
        taskService.create(TestDataHelper.TASK_TITLE,
                           TestDataHelper.TASK_ESTIMATE);
    }

    @Test
    @DataSet(value = "datasets/task.json", cleanBefore = true, cleanAfter = true)
    @ExpectedDataSet("datasets/expected_with_state.json")
    void testUpdateState() {
        taskService.updateState(TASK_ID, States.BACKLOG);
    }
}
