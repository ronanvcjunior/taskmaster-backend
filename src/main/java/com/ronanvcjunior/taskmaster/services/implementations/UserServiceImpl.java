package com.ronanvcjunior.taskmaster.services.implementations;

import com.ronanvcjunior.taskmaster.cache.CacheStore;
import com.ronanvcjunior.taskmaster.domains.RequestContext;
import com.ronanvcjunior.taskmaster.dtos.User;
import com.ronanvcjunior.taskmaster.entities.ConfirmationEntity;
import com.ronanvcjunior.taskmaster.entities.CredentialEntity;
import com.ronanvcjunior.taskmaster.entities.RoleEntity;
import com.ronanvcjunior.taskmaster.entities.UserEntity;
import com.ronanvcjunior.taskmaster.enums.LoginType;
import com.ronanvcjunior.taskmaster.events.UserEvent;
import com.ronanvcjunior.taskmaster.exceptions.ApiException;
import com.ronanvcjunior.taskmaster.repositories.UserRepository;
import com.ronanvcjunior.taskmaster.services.ConfirmationService;
import com.ronanvcjunior.taskmaster.services.CredentialService;
import com.ronanvcjunior.taskmaster.services.RoleService;
import com.ronanvcjunior.taskmaster.services.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ronanvcjunior.taskmaster.enums.Authority.*;
import static com.ronanvcjunior.taskmaster.enums.EventType.REGISTRATION;
import static com.ronanvcjunior.taskmaster.utils.UserUtils.createUserEntity;
import static com.ronanvcjunior.taskmaster.utils.UserUtils.fromUserEntity;
import static java.time.ZonedDateTime.now;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    public static final String USER_NOT_FOUND = "User não encontrado";

    private final UserRepository userRepository;

    private final RoleService roleService;
    private final CredentialService credentialService;
    private final ConfirmationService confirmationService;

    @Qualifier("userLoginCache")
    private final CacheStore<String, Integer> userCache;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void createUser(String firstName, String lastName, String email, String password) {
        UserEntity userEntity = this.createNewUser(firstName, lastName, email);
        this.userRepository.save(userEntity);

        this.credentialService.createCredential(userEntity, password);

        String key = this.confirmationService.createConfirmation(userEntity);

        eventPublisher.publishEvent(new UserEvent(userEntity, REGISTRATION, Map.of("key", key)));
    }

    @Override
    public void verifyAccountKey(String key) {
        ConfirmationEntity confirmationEntity = this.confirmationService.getConfirmationEntityByKey(key);

        UserEntity userEntity = this.getUserEntityByEmail(confirmationEntity.getUser().getEmail());
        userEntity.setEnabled(true);

        this.userRepository.save(userEntity);

        this.confirmationService.delete(confirmationEntity);
    }

    @Override
    public void updateLoginAttempt(String email, LoginType loginType) {
        var userEntity = getUserEntityByEmail(email);

        RequestContext.setUserId(userEntity.getId());

        switch (loginType) {
            case LOGIN_ATTEMPT -> {
                if(userCache.get(userEntity.getEmail()) == null) {
                    userEntity.setLoginAttempts(0);
                    userEntity.setAccountUnlocked(true);
                }

                userEntity.setLoginAttempts(userEntity.getLoginAttempts() + 1);

                userCache.put(userEntity.getEmail(), userEntity.getLoginAttempts());

                if (userCache.get(userEntity.getEmail()) > 5) {
                    userEntity.setAccountUnlocked(false);
                }
            }
            case LOGIN_SUCCESS -> {
                userEntity.setAccountUnlocked(true);
                userEntity.setLoginAttempts(0);
                userEntity.setLastLogin(now());

                userCache.evict(userEntity.getEmail());
            }
        }

        userRepository.save(userEntity);
    }

    @Override
    public User getUserByEmail(String email) {
        UserEntity userEntity = this.getUserEntityByEmail(email);

        CredentialEntity credentialEntity = this.credentialService.getUserCredentialByUserId(userEntity.getUserId());

        return fromUserEntity(userEntity, userEntity.getRole(), credentialEntity);
    }

    @Override
    public User getUserByUserId(String userId) {
        UserEntity userEntity = this.userRepository.findUserEntityByUserId(userId)
                .orElseThrow(() -> new ApiException(USER_NOT_FOUND));

        CredentialEntity credentialEntity = this.credentialService.getUserCredentialByUserId(userEntity.getUserId());

        return fromUserEntity(userEntity, userEntity.getRole(), credentialEntity);
    }

    @Override
    public List<User> getAllUserByUserId() {
        List<UserEntity> usersEntity = this.userRepository.findAllUserEntities()
                .orElseThrow(() -> new ApiException(USER_NOT_FOUND));


        return usersEntity.stream()
                .map(userEntity -> {
                    CredentialEntity credentialEntity = this.credentialService.getUserCredentialByUserId(userEntity.getUserId());
                    return fromUserEntity(userEntity, userEntity.getRole(), credentialEntity);
                })
                .collect(Collectors.toList());
    }

    private UserEntity createNewUser(String firstName, String lastName, String email) {
        RoleEntity role = this.roleService.getRoleName(USER.name());
        return createUserEntity(firstName, lastName, email, role);
    }

    private UserEntity getUserEntityByEmail(String email) {
        Optional<UserEntity> userEntity = this.userRepository.findUserEntitiesByEmailIgnoreCase(email);
        return userEntity.orElseThrow(() -> new ApiException(USER_NOT_FOUND));
    }
}
