package com.ronanvcjunior.taskmaster.dtos.requests;

import jakarta.persistence.Column;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record TaskRequest(
        @Column(nullable = false)
        String name,

        BigDecimal cost,

        @Column(name = "payment_deadline")
        ZonedDateTime paymentDeadline
) {
}
