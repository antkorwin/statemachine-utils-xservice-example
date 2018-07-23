package com.antkorwin.statemachineserviceexample.actions;

import com.antkorwin.commonutils.actions.ActionArgument;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created on 24.07.2018.
 *
 * @author Korovin Anatoliy
 */
@Getter
@AllArgsConstructor
public class CreateTaskActionArgument implements ActionArgument {

    private String taskTitle;
    private int taskEstimate;

    @Override
    public boolean validate() {
        return true;
    }
}
