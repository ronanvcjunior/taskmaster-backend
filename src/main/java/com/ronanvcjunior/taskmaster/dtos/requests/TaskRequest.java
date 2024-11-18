package com.ronanvcjunior.taskmaster.dtos.requests;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record TaskRequest(
        String name,
        BigDecimal cost,
        ZonedDateTime paymentDeadline
) {
}
