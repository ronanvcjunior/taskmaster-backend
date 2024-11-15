package com.ronanvcjunior.taskmaster.events;

import com.ronanvcjunior.taskmaster.entities.UserEntity;
import com.ronanvcjunior.taskmaster.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class UserEvent {
    private UserEntity user;
    private EventType type;
    private Map<?, ?> data;
}
