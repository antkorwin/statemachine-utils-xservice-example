package com.antkorwin.statemachineserviceexample.actions;

import com.antkorwin.commonutils.actions.BaseAction;
import com.antkorwin.statemachineserviceexample.models.Events;
import com.antkorwin.statemachineserviceexample.models.States;
import com.antkorwin.statemachineserviceexample.models.Task;
import com.antkorwin.statemachineserviceexample.services.TaskService;
import com.antkorwin.statemachineutils.service.XStateMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created on 03.08.2018.
 *
 * @author Korovin Anatoliy
 */
@Component
public class ApplyEventAction extends BaseAction<Task, ApplyEventActionArgument> {

    @Autowired
    private XStateMachineService<States, Events> xStateMachineService;

    @Autowired
    private TaskService taskService;

    @Override
    protected Task executeImpl(ApplyEventActionArgument argument) {
        return xStateMachineService
                .evaluateTransactional(argument.getTaskId(),
                                       machine -> {
                                           machine.sendEvent(argument.getEvent());
                                           return taskService.updateState(argument.getTaskId(), machine.getState().getId());
                                       });
    }
}
