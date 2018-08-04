package com.antkorwin.statemachineserviceexample.services;

import com.antkorwin.statemachineserviceexample.models.States;
import com.antkorwin.statemachineserviceexample.models.Task;
import com.antkorwin.statemachineserviceexample.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Created on 23.07.2018.
 *
 * @author Korovin Anatoliy
 */
@Service
public class JpaTaskService implements TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public JpaTaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    @Transactional
    public Task create(String title, int estimate) {
        return taskRepository.save(Task.builder()
                                       .title(title)
                                       .estimate(estimate)
                                       .build());
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Task updateState(UUID taskId, States newState) {
        Task task = taskRepository.getOne(taskId);
        task.setState(newState);
        return taskRepository.save(task);
    }


}
