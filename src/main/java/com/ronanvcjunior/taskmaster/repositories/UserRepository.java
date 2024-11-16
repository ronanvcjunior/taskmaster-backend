package com.ronanvcjunior.taskmaster.repositories;

import com.ronanvcjunior.taskmaster.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.ronanvcjunior.taskmaster.constants.QueryConstants.SELECT_ALL_USERS;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findUserEntitiesByEmailIgnoreCase(String email);

    Optional<UserEntity> findUserEntityByUserId(String userId);

    @Query(value = SELECT_ALL_USERS, nativeQuery = true)
    Optional<List<UserEntity>> findAllUserEntities();
}
