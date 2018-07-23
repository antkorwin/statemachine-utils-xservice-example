package com.antkorwin.statemachineserviceexample.models;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

/**
 * Created on 23.07.2018.
 *
 * @author Korovin Anatoliy
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    private UUID id;

    private String title;

    private int estimate;
}
