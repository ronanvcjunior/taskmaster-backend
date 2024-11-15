package com.ronanvcjunior.taskmaster.services.implementations;

import com.ronanvcjunior.taskmaster.entities.ConfirmationEntity;
import com.ronanvcjunior.taskmaster.entities.UserEntity;
import com.ronanvcjunior.taskmaster.repositories.ConfirmationRepository;
import com.ronanvcjunior.taskmaster.services.ConfirmationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class ConfirmationServiceImpl implements ConfirmationService {
    private final ConfirmationRepository confirmationRepository;

    @Override
    public void createConfirmation(UserEntity userEntity) {
        ConfirmationEntity confirmationEntity = new ConfirmationEntity(userEntity);
        this.confirmationRepository.save(confirmationEntity);
    }
}
