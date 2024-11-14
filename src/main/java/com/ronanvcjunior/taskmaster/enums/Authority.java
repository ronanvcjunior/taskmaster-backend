package com.ronanvcjunior.taskmaster.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.ronanvcjunior.taskmaster.constants.RoleConstants.USER_AUTHORITIES;
import static com.ronanvcjunior.taskmaster.constants.RoleConstants.ADMIN_AUTHORITIES;

@AllArgsConstructor
@Getter
public enum Authority {
    USER(USER_AUTHORITIES),
    ADMIN(ADMIN_AUTHORITIES);

    private final String value;
}
