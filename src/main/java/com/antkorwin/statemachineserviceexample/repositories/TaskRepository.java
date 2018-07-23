package com.antkorwin.statemachineserviceexample.repositories;

import com.antkorwin.statemachineserviceexample.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Created on 23.07.2018.
 *
 * @author Korovin Anatoliy
 */
public interface TaskRepository extends JpaRepository<Task, UUID> {
}
