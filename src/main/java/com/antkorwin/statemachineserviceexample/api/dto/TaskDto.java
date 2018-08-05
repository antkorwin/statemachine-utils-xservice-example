package com.antkorwin.statemachineserviceexample.api.dto;

import com.antkorwin.statemachineserviceexample.models.States;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Created on 05.08.2018.
 *
 * @author Korovin Anatoliy
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class TaskDto {
    private UUID id;
    private String title;
    private int estimate;
    private States state;
}
