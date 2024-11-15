package com.ronanvcjunior.taskmaster.services.implementations;

import com.ronanvcjunior.taskmaster.domains.RequestContext;
import com.ronanvcjunior.taskmaster.entities.RoleEntity;
import com.ronanvcjunior.taskmaster.entities.UserEntity;
import com.ronanvcjunior.taskmaster.repositories.UserRepository;
import com.ronanvcjunior.taskmaster.services.RoleService;
import com.ronanvcjunior.taskmaster.services.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ronanvcjunior.taskmaster.enums.Authority.*;
import static com.ronanvcjunior.taskmaster.utils.UserUtils.createUserEntity;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final RoleService roleService;

    @Override
    public void createUser(String firstName, String lastName, String email, String password) {
        RequestContext.setUserId(0L);

        UserEntity userEntity = this.createNewUser(firstName, lastName, email);
        this.userRepository.save(userEntity);
    }

    private UserEntity createNewUser(String firstName, String lastName, String email) {
        RoleEntity role = this.roleService.getRoleName(USER.name());
        return createUserEntity(firstName, lastName, email, role);
    }
}
