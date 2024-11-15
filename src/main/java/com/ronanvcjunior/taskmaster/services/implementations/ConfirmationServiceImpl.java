package com.ronanvcjunior.taskmaster.services.implementations;

import com.ronanvcjunior.taskmaster.entities.ConfirmationEntity;
import com.ronanvcjunior.taskmaster.entities.UserEntity;
import com.ronanvcjunior.taskmaster.exceptions.ApiException;
import com.ronanvcjunior.taskmaster.repositories.ConfirmationRepository;
import com.ronanvcjunior.taskmaster.services.ConfirmationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class ConfirmationServiceImpl implements ConfirmationService {
    public static final String CONFIRMATION_NOT_FOUND = "Confirmation não encontrado";

    private final ConfirmationRepository confirmationRepository;

    @Override
    public String createConfirmation(UserEntity userEntity) {
        ConfirmationEntity confirmationEntity = new ConfirmationEntity(userEntity);
        this.confirmationRepository.save(confirmationEntity);
        return confirmationEntity.getKey();
    }

    @Override
    public ConfirmationEntity getConfirmationEntityByKey(String key) {
        Optional<ConfirmationEntity> confirmationEntity = this.confirmationRepository.findConfirmationEntityByKey(key);
        return confirmationEntity.orElseThrow(() -> new ApiException(CONFIRMATION_NOT_FOUND));
    }

    @Override
    public void delete(ConfirmationEntity confirmationEntity) {
        this.confirmationRepository.delete(confirmationEntity);
    }
}
