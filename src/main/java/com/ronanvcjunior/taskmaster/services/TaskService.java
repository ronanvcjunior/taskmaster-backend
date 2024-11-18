package com.ronanvcjunior.taskmaster.services;

import com.ronanvcjunior.taskmaster.dtos.User;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public interface TaskService {
    void createTask(String name, BigDecimal cost, ZonedDateTime paymentDeadline, User user);
}
