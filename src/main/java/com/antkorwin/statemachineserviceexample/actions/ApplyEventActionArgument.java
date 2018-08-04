package com.antkorwin.statemachineserviceexample.actions;

import com.antkorwin.commonutils.actions.ActionArgument;
import com.antkorwin.statemachineserviceexample.models.Events;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

/**
 * Created on 03.08.2018.
 *
 * @author Korovin Anatoliy
 */
@AllArgsConstructor
@Getter
public class ApplyEventActionArgument implements ActionArgument {

    private final UUID taskId;
    private final Events event;

    @Override
    public boolean validate() {
        return taskId != null && event != null;
    }
}
