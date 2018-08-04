package com.antkorwin.statemachineserviceexample.actions;

import com.antkorwin.commonutils.actions.Action;
import com.antkorwin.statemachineserviceexample.models.Events;
import com.antkorwin.statemachineserviceexample.models.States;
import com.antkorwin.statemachineserviceexample.models.Task;
import com.antkorwin.statemachineserviceexample.services.TaskService;
import com.antkorwin.statemachineutils.service.XStateMachineService;
import org.junit.jupiter.api.Test;
import org.springframework.statemachine.StateMachine;

import static com.antkorwin.statemachineserviceexample.TestDataHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

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
    void testCreateTaskAction() {
        // Arrange
        when(taskService.create(eq(TASK_TITLE), eq(TASK_ESTIMATE))).thenReturn(TASK);

        StateMachine<States, Events> mockMachine = mock(StateMachine.class, RETURNS_DEEP_STUBS);
        when(mockMachine.getState().getId())
                .thenReturn(States.BACKLOG);

        when(xStateMachineService.create(any())).thenReturn(mockMachine);

        when(taskService.updateState(eq(TASK.getId()), eq(States.BACKLOG)))
                .thenReturn(TASK);

        // Act
        Task task = createTaskAction
                .execute(new CreateTaskActionArgument(TASK_TITLE,
                                                      TASK_ESTIMATE));

        // Assert
        assertThat(task).isNotNull().isEqualTo(TASK);
        verify(taskService).create(TASK_TITLE, TASK_ESTIMATE);
        verify(xStateMachineService).create(eq(TASK.getId()));
        verify(taskService).updateState(TASK.getId(), States.BACKLOG);
    }
}
