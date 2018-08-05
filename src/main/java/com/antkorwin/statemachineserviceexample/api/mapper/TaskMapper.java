package com.antkorwin.statemachineserviceexample.api.mapper;

import com.antkorwin.statemachineserviceexample.api.dto.TaskDto;
import com.antkorwin.statemachineserviceexample.models.Task;
import org.mapstruct.Mapper;

/**
 * Created on 05.08.2018.
 *
 * @author Korovin Anatoliy
 */
@Mapper
public interface TaskMapper {

    TaskDto toDto(Task task);
}
