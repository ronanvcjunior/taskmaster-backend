package com.ronanvcjunior.taskmaster.dtos;

import lombok.Builder;

@Builder
public record User(
        Long id,
        Long createdBy,
        Long updatedBy,
        String userId,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String bio,
        String imageUrl,
        String lastLogin,
        String createdAt,
        String updatedAt,
        String role,
        String authorities,
        boolean accountUnexpired,
        boolean accountUnlocked,
        boolean credentialUnexpired,
        boolean enabled
) { }
