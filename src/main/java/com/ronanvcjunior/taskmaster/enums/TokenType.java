package com.ronanvcjunior.taskmaster.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TokenType {
    ACCESS("access-token"), REFRESH("refresh-token");

    private final String value;
}