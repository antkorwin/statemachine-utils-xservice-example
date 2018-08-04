package com.antkorwin.statemachineserviceexample.services;

import com.antkorwin.statemachineserviceexample.models.States;
import com.antkorwin.statemachineserviceexample.models.Task;

import java.util.UUID;

/**
 * Created on 23.07.2018.
 *
 * @author Korovin Anatoliy
 */
public interface TaskService {
    Task create(String title, int estimate);

    Task updateState(UUID taskId, States newState);
}
