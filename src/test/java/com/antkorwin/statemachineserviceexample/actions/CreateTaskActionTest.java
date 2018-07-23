package com.antkorwin.statemachineserviceexample.actions;

import com.antkorwin.commonutils.actions.Action;
import com.antkorwin.commonutils.actions.ActionArgument;
import com.antkorwin.statemachineserviceexample.TestDataHelper;
import com.antkorwin.statemachineserviceexample.models.Events;
import com.antkorwin.statemachineserviceexample.models.States;
import com.antkorwin.statemachineserviceexample.models.Task;
import com.antkorwin.statemachineserviceexample.services.TaskService;
import com.antkorwin.statemachineutils.service.XStateMachineService;
import org.junit.jupiter.api.Test;

import static com.antkorwin.statemachineserviceexample.TestDataHelper.TASK;
import static com.antkorwin.statemachineserviceexample.TestDataHelper.TASK_ESTIMATE;
import static com.antkorwin.statemachineserviceexample.TestDataHelper.TASK_TITLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created on 24.07.2018.
 *
 * @author Korovin Anatoliy
 */
public class CreateTaskActionTest {

    private TaskService taskService = mock(TaskService.class);
    private XStateMachineService<States, Events> xStateMachineService = mock(XStateMachineService.class);
    private Action<Task> createTaskAction = new CreateTaskAction(taskService, xStateMachineService);


    @Test
    void testExecute() {
        // Arrange
        ActionArgument argument = new CreateTaskActionArgument(TASK_TITLE,
                                                               TASK_ESTIMATE);

        when(taskService.create(eq(TASK_TITLE), eq(TASK_ESTIMATE)))
                .thenReturn(TASK);

        // Act
        Task task = createTaskAction.execute(argument);

        // Asserts
        assertThat(task).isNotNull()
                        .isEqualTo(TASK);

        verify(taskService).create(TASK_TITLE,
                                   TASK_ESTIMATE);

        verify(xStateMachineService).create(eq(TASK.getId()));
    }
}
