package com.ronanvcjunior.taskmaster.repositories;

import com.ronanvcjunior.taskmaster.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository  extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findRoleEntityByNameIgnoreCase(String name);
}
