package com.antkorwin.statemachineserviceexample;

import com.antkorwin.statemachineserviceexample.models.Task;

import java.util.UUID;

/**
 * Created on 23.07.2018.
 *
 * @author Korovin Anatoliy
 */
public class TestDataHelper {

    public static final int TASK_ESTIMATE = 20;
    public static final String TASK_TITLE = "title";
    public static final Task TASK = Task.builder()
                                        .title(TASK_TITLE)
                                        .id(UUID.randomUUID())
                                        .estimate(TASK_ESTIMATE)
                                        .build();
}
