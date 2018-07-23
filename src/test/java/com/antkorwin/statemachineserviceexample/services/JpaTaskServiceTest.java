package com.antkorwin.statemachineserviceexample.services;

import com.antkorwin.statemachineserviceexample.models.Task;
import com.antkorwin.statemachineserviceexample.repositories.TaskRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static com.antkorwin.statemachineserviceexample.TestDataHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Created on 23.07.2018.
 *
 * @author Korovin Anatoliy
 */
public class JpaTaskServiceTest {

    private TaskRepository taskRepository = mock(TaskRepository.class);
    private TaskService taskService = new JpaTaskService(taskRepository);


    @Test
    void testCreateTask() {
        // Arrange
        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        when(taskRepository.save(any(Task.class))).thenReturn(TASK);
        // Act
        Task task = taskService.create(TASK_TITLE, TASK_ESTIMATE);

        // Asserts
        assertThat(task).isNotNull()
                        .isEqualTo(TASK);

        verify(taskRepository).save(captor.capture());
        assertThat(captor.getValue())
                .extracting(Task::getTitle, Task::getEstimate)
                .contains(TASK_TITLE, TASK_ESTIMATE);
    }
}
