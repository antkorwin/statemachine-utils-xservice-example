package com.antkorwin.statemachineserviceexample.actions;

import com.antkorwin.commonutils.actions.Action;
import com.antkorwin.junit5integrationtestutils.test.runners.EnableH2;
import com.antkorwin.junit5integrationtestutils.test.runners.EnableIntegrationTests;
import com.antkorwin.junit5integrationtestutils.test.runners.EnableRiderTests;
import com.antkorwin.statemachineserviceexample.models.Events;
import com.antkorwin.statemachineserviceexample.models.States;
import com.antkorwin.statemachineserviceexample.models.Task;
import com.antkorwin.statemachineutils.service.XStateMachineService;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;

import java.util.UUID;

import static com.antkorwin.statemachineserviceexample.TestDataHelper.TASK_ESTIMATE;
import static com.antkorwin.statemachineserviceexample.TestDataHelper.TASK_TITLE;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 24.07.2018.
 *
 * @author Korovin Anatoliy
 */
@EnableIntegrationTests
@EnableRiderTests
@EnableH2
public class CreateTaskActionIT {

    @Autowired
    @Qualifier("createTaskAction")
    private Action<Task> createTaskAction;

    @Autowired
    private XStateMachineService<States, Events> xStateMachineService;

    @Autowired
    private StateMachinePersist<States, Events, UUID> persist;

    @Test
    void testDI() {
        assertThat(createTaskAction).isNotNull();
    }

    @Test
    @DataSet(cleanAfter = true, cleanBefore = true)
    @ExpectedDataSet("datasets/expected_with_state.json")
    void testCompletely_ExecuteCreateTaskAction() throws Exception {
        // Arrange
        CreateTaskActionArgument argument = new CreateTaskActionArgument(TASK_TITLE,
                                                                         TASK_ESTIMATE);

        // Act
        Task task = createTaskAction.execute(argument);

        // Asserts
        assertThat(task).isNotNull()
                        .as("check a returned value from the CreateTaskAction")
                        .extracting(Task::getTitle,
                                    Task::getEstimate,
                                    Task::getState)
                        .contains(TASK_TITLE,
                                  TASK_ESTIMATE,
                                  States.BACKLOG);

        StateMachine<States, Events> machine = xStateMachineService.get(task.getId());
        assertThat(machine).as("check a current state for created machine.")
                           .isNotNull()
                           .extracting(s -> s.getState().getId())
                           .contains(States.BACKLOG);

        assertThat(machine)
                .as("check that an id of the created machine is equal to task.id")
                .extracting(StateMachine::getId)
                .contains(task.getId().toString());

        StateMachineContext<States, Events> machineContext = persist.read(task.getId());
        assertThat(machineContext)
                .as("test a side effect on the persist level")
                .extracting(StateMachineContext::getId,
                            StateMachineContext::getState)
                .contains(task.getId().toString(),
                          States.BACKLOG);
    }

    @Test
    void testCreateStateMachineInstance_AfterExecuteCreateTaskAction() {
        // Arrange
        // Act
        Task task = createTaskAction.execute(new CreateTaskActionArgument(TASK_TITLE,
                                                                          TASK_ESTIMATE));
        // Asserts
        StateMachine<States, Events> machine = xStateMachineService.get(task.getId());
        assertThat(machine).as("check a current state for created machine.")
                           .isNotNull()
                           .extracting(s -> s.getState().getId())
                           .contains(States.BACKLOG);
    }


    @Test
    @DataSet(cleanAfter = true, cleanBefore = true)
    @ExpectedDataSet("datasets/expected_with_state.json")
    void testCreateTaskInDatabase_AfterExecuteCreateTaskAction() {

        createTaskAction.execute(new CreateTaskActionArgument(TASK_TITLE,
                                                              TASK_ESTIMATE));
    }
}
