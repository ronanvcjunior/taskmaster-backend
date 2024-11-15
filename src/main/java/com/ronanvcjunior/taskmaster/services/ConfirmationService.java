package com.ronanvcjunior.taskmaster.services;

import com.ronanvcjunior.taskmaster.entities.UserEntity;

public interface ConfirmationService {
    void createConfirmation(UserEntity userEntity);
}
