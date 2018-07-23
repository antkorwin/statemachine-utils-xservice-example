package com.antkorwin.statemachineserviceexample.configs;

import com.antkorwin.statemachineserviceexample.models.Events;
import com.antkorwin.statemachineserviceexample.models.States;
import com.antkorwin.statemachineutils.service.EnableStateMachineXService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.*;

/**
 * Created on 23.07.2018.
 *
 * @author Korovin Anatoliy
 */
@Slf4j
@Configuration
@EnableStateMachineFactory
@EnableStateMachineXService
public class StateMachineConfig
        extends EnumStateMachineConfigurerAdapter<States, Events> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<States, Events> config) throws Exception {
        config.withConfiguration()
              .autoStartup(true);
    }

    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {
        states.withStates()
              .initial(States.BACKLOG)
              .state(States.IN_PROGRESS)
              .state(States.DONE);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {
        transitions.withExternal()
                   .source(States.BACKLOG)
                   .target(States.IN_PROGRESS)
                   .event(Events.START_FEATURE)
                   .and()
                   .withExternal()
                   .source(States.IN_PROGRESS)
                   .target(States.DONE)
                   .event(Events.FINISH_FEATURE);
    }
}
