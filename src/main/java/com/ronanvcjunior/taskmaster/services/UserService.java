package com.ronanvcjunior.taskmaster.services;

import com.ronanvcjunior.taskmaster.dtos.User;
import com.ronanvcjunior.taskmaster.enums.LoginType;

public interface UserService {
    void createUser(String firstName, String lastName, String email, String password);

    void verifyAccountKey(String key);

    void updateLoginAttempt(String email, LoginType loginType);

    User getUserByEmail(String email);
}
