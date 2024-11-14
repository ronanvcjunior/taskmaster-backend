package com.ronanvcjunior.taskmaster.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@JsonInclude(NON_DEFAULT)
public class UserEntity extends Auditable{

    @Column(unique = true, updatable = false, nullable = false)
    private String userId;

    private String firstName;
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    private Integer loginAttempts;
    private ZonedDateTime lastLogin;
    private boolean accountUnexpired;
    private boolean accountUnlocked;
    private boolean enabled;
    private String role;
}
