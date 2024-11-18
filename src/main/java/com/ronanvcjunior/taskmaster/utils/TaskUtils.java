package com.ronanvcjunior.taskmaster.utils;

import com.ronanvcjunior.taskmaster.dtos.response.TaskResponse;
import com.ronanvcjunior.taskmaster.entities.TaskEntity;
import com.ronanvcjunior.taskmaster.entities.UserEntity;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import static java.time.ZonedDateTime.now;

public class TaskUtils {
    public static TaskEntity createTaskEntity(String name, BigDecimal cost, ZonedDateTime paymentDeadline, Integer order, UserEntity user) {
        return TaskEntity.builder()
                .taskId(UUID.randomUUID().toString())
                .name(name)
                .cost(cost)
                .paymentDeadline(paymentDeadline)
                .order(order)
                .owner(user)
                .build();

    }

    public static TaskResponse fromTaskEntity(TaskEntity taskEntity) {
        return TaskResponse.builder()
                .taskId(taskEntity.getTaskId())
                .name(taskEntity.getName())
                .cost(taskEntity.getCost())
                .paymentDeadline(taskEntity.getPaymentDeadline())
                .order(taskEntity.getOrder())
                .build();
    }
}
