package com.ronanvcjunior.taskmaster.utils;

import com.ronanvcjunior.taskmaster.dtos.User;
import com.ronanvcjunior.taskmaster.entities.CredentialEntity;
import com.ronanvcjunior.taskmaster.entities.RoleEntity;
import com.ronanvcjunior.taskmaster.entities.UserEntity;

import java.util.UUID;

import static com.ronanvcjunior.taskmaster.constants.SecurityConstants.NINETY_DAYS;
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

    public static User fromUserEntity(UserEntity userEntity, RoleEntity role, CredentialEntity credentialEntity) {
        return User.builder()
                .id(userEntity.getId())
                .createdBy(userEntity.getCreatedBy())
                .updatedBy(userEntity.getUpdatedBy())
                .userId(userEntity.getUserId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .lastLogin(userEntity.getLastLogin().toString())
                .createdAt(userEntity.getCreatedAt().toString())
                .updatedAt(userEntity.getUpdatedAt().toString())
                .role(role.getName())
                .authorities(role.getAuthorities().getValue())
                .accountUnexpired(userEntity.isAccountUnexpired())
                .accountUnlocked(userEntity.isAccountUnlocked())
                .credentialUnexpired(isCredentialsNonExpired(credentialEntity))
                .enabled(userEntity.isEnabled())
                .build();
    }

    public static boolean isCredentialsNonExpired(CredentialEntity credentialEntity) {
        return credentialEntity.getUpdatedAt().plusDays(NINETY_DAYS).isAfter(now());
    }

}
