package com.ronanvcjunior.taskmaster.dtos;

import lombok.Builder;

@Builder
public record Token(String access, String refresh) {
}
