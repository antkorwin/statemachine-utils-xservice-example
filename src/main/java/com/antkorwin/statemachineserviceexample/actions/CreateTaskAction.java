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
 * Created on 23.07.2018.
 *
 * @author Korovin Anatoliy
 */
@Component
public class CreateTaskAction extends BaseAction<Task, CreateTaskActionArgument> {

    private TaskService taskService;
    private XStateMachineService<States, Events> xStateMachineService;

    @Autowired
    public CreateTaskAction(TaskService taskService,
                            XStateMachineService<States, Events> xStateMachineService) {
        this.taskService = taskService;
        this.xStateMachineService = xStateMachineService;
    }


    @Override
    protected Task executeImpl(CreateTaskActionArgument argument) {
        Task task = taskService.create(argument.getTaskTitle(), argument.getTaskEstimate());
        xStateMachineService.create(task.getId());
        return task;
    }
}
