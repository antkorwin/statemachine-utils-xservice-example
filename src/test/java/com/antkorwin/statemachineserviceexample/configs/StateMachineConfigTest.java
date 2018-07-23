package com.antkorwin.statemachineserviceexample.configs;

import com.antkorwin.statemachineserviceexample.models.Events;
import com.antkorwin.statemachineserviceexample.models.States;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 23.07.2018.
 *
 * @author Korovin Anatoliy
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
class StateMachineConfigTest {

    @Autowired
    private StateMachineFactory<States, Events> stateMachineFactory;

    @Test
    void testFactory() {
        // Act
        StateMachine<States, Events> stateMachine = stateMachineFactory.getStateMachine();
        // Asserts
        assertThat(stateMachine).isNotNull()
                                .extracting(s -> s.getState().getId())
                                .contains(States.BACKLOG);
    }
}