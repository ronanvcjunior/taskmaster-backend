package com.ronanvcjunior.taskmaster.services.implementations;

import com.ronanvcjunior.taskmaster.domains.RequestContext;
import com.ronanvcjunior.taskmaster.entities.RoleEntity;
import com.ronanvcjunior.taskmaster.entities.UserEntity;
import com.ronanvcjunior.taskmaster.events.UserEvent;
import com.ronanvcjunior.taskmaster.repositories.UserRepository;
import com.ronanvcjunior.taskmaster.services.ConfirmationService;
import com.ronanvcjunior.taskmaster.services.CredentialService;
import com.ronanvcjunior.taskmaster.services.RoleService;
import com.ronanvcjunior.taskmaster.services.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.ronanvcjunior.taskmaster.enums.Authority.*;
import static com.ronanvcjunior.taskmaster.enums.EventType.REGISTRATION;
import static com.ronanvcjunior.taskmaster.utils.UserUtils.createUserEntity;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final RoleService roleService;
    private final CredentialService credentialService;
    private final ConfirmationService confirmationService;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void createUser(String firstName, String lastName, String email, String password) {
        RequestContext.setUserId(0L);

        UserEntity userEntity = this.createNewUser(firstName, lastName, email);
        this.userRepository.save(userEntity);

        this.credentialService.createCredential(userEntity, password);

        String key = this.confirmationService.createConfirmation(userEntity);

        eventPublisher.publishEvent(new UserEvent(userEntity, REGISTRATION, Map.of("key", key)));
    }

    private UserEntity createNewUser(String firstName, String lastName, String email) {
        RoleEntity role = this.roleService.getRoleName(USER.name());
        return createUserEntity(firstName, lastName, email, role);
    }
}