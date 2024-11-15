package com.ronanvcjunior.taskmaster.utils;

import com.ronanvcjunior.taskmaster.entities.RoleEntity;
import com.ronanvcjunior.taskmaster.entities.UserEntity;

import java.util.UUID;

import static java.time.ZonedDateTime.now;

public class UserUtils {
    public static UserEntity createUserEntity(String firstName, String lastName, String email, RoleEntity role) {
        return UserEntity.builder()
                .userId(UUID.randomUUID().toString())
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .lastLogin(now())
                .accountUnexpired(true)
                .accountUnlocked(true)
                .enabled(false)
                .loginAttempts(0)
                .role(role)
                .build();

    }

}
