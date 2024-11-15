package com.ronanvcjunior.taskmaster.services;

import com.ronanvcjunior.taskmaster.entities.ConfirmationEntity;
import com.ronanvcjunior.taskmaster.entities.UserEntity;

public interface ConfirmationService {
    String createConfirmation(UserEntity userEntity);

    ConfirmationEntity getConfirmationEntityByKey(String key);

    void delete(ConfirmationEntity confirmationEntity);
}
