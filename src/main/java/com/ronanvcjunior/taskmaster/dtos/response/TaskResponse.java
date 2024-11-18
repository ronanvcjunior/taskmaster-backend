package com.ronanvcjunior.taskmaster.dtos.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Builder
public record TaskResponse(
        String taskId,
        String name,
        BigDecimal cost,
        ZonedDateTime paymentDeadline,
        Integer order
) { }
