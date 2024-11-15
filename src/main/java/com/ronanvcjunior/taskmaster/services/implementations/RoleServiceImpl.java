package com.ronanvcjunior.taskmaster.services.implementations;

import com.ronanvcjunior.taskmaster.entities.RoleEntity;
import com.ronanvcjunior.taskmaster.exceptions.ApiException;
import com.ronanvcjunior.taskmaster.repositories.RoleRepository;
import com.ronanvcjunior.taskmaster.services.RoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private static final String ROLE_NOT_FOUND = "Role não encontrada";

    private final RoleRepository roleRepository;

    @Override
    public RoleEntity getRoleName(String name) {
        Optional<RoleEntity> role = this.roleRepository.findRoleEntityByNameIgnoreCase(name);
        return role.orElseThrow(() -> new ApiException(ROLE_NOT_FOUND));
    }
}
