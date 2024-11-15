package com.ronanvcjunior.taskmaster.services;

import com.ronanvcjunior.taskmaster.entities.UserEntity;

public interface CredentialService {
    void createCredential(UserEntity userEntity, String password);
}
