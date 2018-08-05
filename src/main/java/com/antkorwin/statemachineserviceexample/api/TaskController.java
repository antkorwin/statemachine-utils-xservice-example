package com.antkorwin.statemachineserviceexample.api;

import com.antkorwin.commonutils.actions.Action;
import com.antkorwin.statemachineserviceexample.actions.ApplyEventActionArgument;
import com.antkorwin.statemachineserviceexample.actions.CreateTaskActionArgument;
import com.antkorwin.statemachineserviceexample.api.dto.TaskDto;
import com.antkorwin.statemachineserviceexample.api.mapper.TaskMapper;
import com.antkorwin.statemachineserviceexample.models.Events;
import com.antkorwin.statemachineserviceexample.models.States;
import com.antkorwin.statemachineserviceexample.models.Task;
import com.antkorwin.statemachineutils.service.XStateMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Created on 05.08.2018.
 *
 * @author Korovin Anatoliy
 */
@RestController
@RequestMapping("tasks")
public class TaskController {

    @Autowired
    @Qualifier("createTaskAction")
    private Action<Task> createTaskAction;

    @Autowired
    @Qualifier("applyEventAction")
    private Action<Task> applyEventAction;

    @Autowired
    private XStateMachineService<States, Events> xStateMachineService;

    @Autowired
    private TaskMapper taskMapper;

    @PostMapping("create")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto createTask(String title, int estimate) {

        Task task = createTaskAction.execute(
                new CreateTaskActionArgument(title, estimate));

        return taskMapper.toDto(task);
    }

    @PostMapping("/{taskId}/send-event")
    public TaskDto sendEvent(@PathVariable("taskId") UUID taskId,
                             @RequestParam("event") Events event) {

        Task task = applyEventAction.execute(
                new ApplyEventActionArgument(taskId, event));

        return taskMapper.toDto(task);
    }

    @GetMapping("/{taskId}/events")
    public List<Events> getEvents(@PathVariable("taskId") UUID taskId){
        return xStateMachineService.retrieveAvailableEvents(taskId);
    }
}
