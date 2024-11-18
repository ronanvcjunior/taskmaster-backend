package com.ronanvcjunior.taskmaster.services;

import com.ronanvcjunior.taskmaster.dtos.User;
import com.ronanvcjunior.taskmaster.dtos.response.TaskResponse;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public interface TaskService {
    void createTask(String name, BigDecimal cost, ZonedDateTime paymentDeadline);

    TaskResponse getTask(@Valid String taskId);
}
