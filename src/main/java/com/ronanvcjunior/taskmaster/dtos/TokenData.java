package com.ronanvcjunior.taskmaster.dtos;

import io.jsonwebtoken.Claims;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Builder
public record TokenData(
        User user,
        Claims claims,
        boolean valid,
        List<GrantedAuthority> authorities
) {
}
