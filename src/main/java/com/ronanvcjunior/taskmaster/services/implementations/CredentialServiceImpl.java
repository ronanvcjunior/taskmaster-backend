package com.ronanvcjunior.taskmaster.services.implementations;

import com.ronanvcjunior.taskmaster.entities.CredentialEntity;
import com.ronanvcjunior.taskmaster.entities.UserEntity;
import com.ronanvcjunior.taskmaster.repositories.CredentialRepository;
import com.ronanvcjunior.taskmaster.services.CredentialService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class CredentialServiceImpl implements CredentialService {
    private final CredentialRepository credentialRepository;

    private final BCryptPasswordEncoder encoder;

    @Override
    public void createCredential(UserEntity userEntity, String password) {
        CredentialEntity credentialEntity = new CredentialEntity(userEntity, encoder.encode(password));
        this.credentialRepository.save(credentialEntity);
    }
}
