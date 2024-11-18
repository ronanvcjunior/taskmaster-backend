package com.ronanvcjunior.taskmaster.services;

import com.ronanvcjunior.taskmaster.dtos.User;
import com.ronanvcjunior.taskmaster.dtos.response.TaskResponse;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public interface TaskService {
    void createTask(String name, BigDecimal cost, ZonedDateTime paymentDeadline);

    TaskResponse getTask(@Valid String taskId);

    List<TaskResponse> getAllTasks();

    void moveTaskOrder(@Valid String taskId, @Valid Boolean moveUp);

    void setTaskOrder(@Valid String taskId, @Valid Integer order);

    void updateTask(String taskId, String name, BigDecimal cost, ZonedDateTime paymentDeadline);

    void deleteTask(@Valid String taskId);
}
