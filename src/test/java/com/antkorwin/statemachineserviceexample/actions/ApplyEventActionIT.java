package com.antkorwin.statemachineserviceexample.actions;

import com.antkorwin.junit5integrationtestutils.test.runners.EnableH2;
import com.antkorwin.junit5integrationtestutils.test.runners.EnableIntegrationTests;
import com.antkorwin.junit5integrationtestutils.test.runners.EnableRiderTests;
import com.antkorwin.statemachineserviceexample.models.Events;
import com.antkorwin.statemachineserviceexample.models.States;
import com.antkorwin.statemachineserviceexample.repositories.TaskRepository;
import com.antkorwin.statemachineutils.service.XStateMachineService;
import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;

import java.util.UUID;

import static com.antkorwin.statemachineserviceexample.TestDataHelper.TASK_ID;
import static com.antkorwin.statemachineserviceexample.models.Events.START_FEATURE;
import static com.antkorwin.statemachineserviceexample.models.States.BACKLOG;
import static com.antkorwin.statemachineserviceexample.models.States.IN_PROGRESS;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 03.08.2018.
 *
 * @author Korovin Anatoliy
 */
@EnableIntegrationTests
@EnableRiderTests
@EnableH2
public class ApplyEventActionIT {

    @Autowired
    private ApplyEventAction action;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private StateMachinePersister<States, Events, UUID> persister;

    @Autowired
    private StateMachineFactory<States, Events> factory;

    @Autowired
    private XStateMachineService<States, Events> xStateMachineService;


    @BeforeEach
    void setUp() throws Exception {
        StateMachine<States, Events> mockMachine = factory.getStateMachine(TASK_ID.toString());
        persister.persist(mockMachine, TASK_ID);
    }

    @Test
    @DataSet("datasets/task.json")
    void testChangeTaskStateInDatabase_AfterApplyEvent() {
        // Act
        action.execute(new ApplyEventActionArgument(TASK_ID, START_FEATURE));

        // Asserts
        States state = taskRepository.findById(TASK_ID).get().getState();
        assertThat(state).isEqualTo(IN_PROGRESS);
    }

    @Test
    @DataSet("datasets/task.json")
    void testChangeMachineState_AfterApplyEvent() {
        // Arrange
        StateMachine<States, Events> machine = xStateMachineService.get(TASK_ID);

        // Precondition
        assertThat(machine.getState().getId()).isEqualTo(BACKLOG);

        // Act
        action.execute(new ApplyEventActionArgument(TASK_ID, START_FEATURE));

        // Asserts
        machine = xStateMachineService.get(TASK_ID);
        assertThat(machine.getState().getId()).isEqualTo(IN_PROGRESS);
    }
}
