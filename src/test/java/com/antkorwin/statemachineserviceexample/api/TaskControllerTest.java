package com.antkorwin.statemachineserviceexample.api;

import com.antkorwin.junit5integrationtestutils.mvc.MvcRequester;
import com.antkorwin.junit5integrationtestutils.test.runners.EnableH2;
import com.antkorwin.junit5integrationtestutils.test.runners.EnableIntegrationTests;
import com.antkorwin.junit5integrationtestutils.test.runners.EnableRestTests;
import com.antkorwin.junit5integrationtestutils.test.runners.EnableRiderTests;
import com.antkorwin.statemachineserviceexample.TestDataHelper;
import com.antkorwin.statemachineserviceexample.api.dto.TaskDto;
import com.antkorwin.statemachineserviceexample.models.Events;
import com.antkorwin.statemachineserviceexample.models.States;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static com.antkorwin.statemachineserviceexample.TestDataHelper.TASK_ID;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 05.08.2018.
 *
 * @author Korovin Anatoliy
 */
@EnableIntegrationTests
@EnableRestTests
@EnableRiderTests
@EnableH2
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StateMachinePersister<States, Events, UUID> persister;

    @Autowired
    private StateMachineFactory<States, Events> factory;

    @BeforeEach
    void setUp() throws Exception {
        StateMachine<States, Events> mockMachine = factory.getStateMachine(TASK_ID.toString());
        persister.persist(mockMachine, TASK_ID);
    }

    @Test
    @DataSet(cleanBefore = true, cleanAfter = true)
    @ExpectedDataSet("datasets/expected_with_state.json")
    void testCreateTask() throws Exception {
        // Act
        TaskDto taskDto = MvcRequester.on(mockMvc)
                                      .to("/tasks/create")
                                      .withParam("title", TestDataHelper.TASK_TITLE)
                                      .withParam("estimate", TestDataHelper.TASK_ESTIMATE)
                                      .post()
                                      .expectStatus(HttpStatus.CREATED)
                                      .returnAs(TaskDto.class);
        // Assert
        assertThat(taskDto).isNotNull()
                           .extracting(TaskDto::getTitle,
                                       TaskDto::getEstimate,
                                       TaskDto::getState)
                           .contains(TestDataHelper.TASK_TITLE,
                                     TestDataHelper.TASK_ESTIMATE,
                                     States.BACKLOG);

        assertThat(taskDto.getId()).isNotNull();
    }

    @Test
    @DataSet(value = "datasets/task.json", cleanAfter = true, cleanBefore = true)
    void testApplyEvent() throws Exception {
        // Act
        TaskDto taskDto = MvcRequester.on(mockMvc)
                                      .to("/tasks/{id}/send-event", TestDataHelper.TASK_ID)
                                      .withParam("event", Events.START_FEATURE)
                                      .post()
                                      .expectStatus(HttpStatus.OK)
                                      .returnAs(TaskDto.class);
        // Asserts
        assertThat(taskDto).isNotNull()
                           .extracting(TaskDto::getState)
                           .contains(States.IN_PROGRESS);
    }

    @Test
    @DataSet(value = "datasets/task.json", cleanAfter = true, cleanBefore = true)
    void testResolveAvailableEvents() throws Exception {
        // Arrange
        // Act
        List<Events> events = MvcRequester.on(mockMvc)
                                          .to("/tasks/{id}/events", TestDataHelper.TASK_ID)
                                          .get()
                                          .expectStatus(HttpStatus.OK)
                                          .doReturn(new TypeReference<List<Events>>() {
                                          });
        // Asserts
        assertThat(events)
                .containsOnly(Events.START_FEATURE);
    }
}