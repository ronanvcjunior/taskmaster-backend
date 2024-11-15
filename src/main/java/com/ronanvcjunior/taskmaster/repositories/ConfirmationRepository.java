package com.ronanvcjunior.taskmaster.repositories;

import com.ronanvcjunior.taskmaster.entities.ConfirmationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationRepository extends JpaRepository<ConfirmationEntity, Long> {
    Optional<ConfirmationEntity> findConfirmationEntityByKey(String key);

    Optional<ConfirmationEntity> findConfirmationEntityByUserId(Long userId);
}
