package com.ronanvcjunior.taskmaster.securities.config;

import com.ronanvcjunior.taskmaster.domains.ApiAuthentication;
import com.ronanvcjunior.taskmaster.domains.UserPrincipal;
import com.ronanvcjunior.taskmaster.dtos.User;
import com.ronanvcjunior.taskmaster.entities.CredentialEntity;
import com.ronanvcjunior.taskmaster.exceptions.ApiException;
import com.ronanvcjunior.taskmaster.services.CredentialService;
import com.ronanvcjunior.taskmaster.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.function.Function;

import static com.ronanvcjunior.taskmaster.constants.SecurityConstants.NINETY_DAYS;
import static java.time.ZonedDateTime.now;

@Component
@RequiredArgsConstructor
public class ApiAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final CredentialService credentialService;

    private final BCryptPasswordEncoder encoder;

    private final Function<Authentication, ApiAuthentication> authenticationFunction = authentication -> (ApiAuthentication) authentication;

    private final Consumer<UserPrincipal> validAccount = userPrincipal -> {
        if(!userPrincipal.isAccountNonLocked()) {throw new LockedException("Sua conta está bloqueada no momento");}
        if(!userPrincipal.isEnabled()) {throw new DisabledException("Sua conta está desativada no momento");}
        if(!userPrincipal.isCredentialsNonExpired()) {throw new CredentialsExpiredException("As credenciais da sua conta expiraram, entre em contato com o administrador da conta");}
        if(!userPrincipal.isAccountNonExpired()) {throw new DisabledException("Sua conta expirou. Entre em contato com o administrador da conta");}
    };

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        ApiAuthentication apiAuthentication = authenticationFunction.apply(authentication);

        User user = this.userService.getUserByEmail(apiAuthentication.getEmail());

        if (user != null) {
            CredentialEntity userCredential = this.credentialService.getUserCredentialByUserId(user.userId());

            if(userCredential.getUpdatedAt().minusDays(NINETY_DAYS).isAfter(now())) {
                throw new ApiException("As credenciais estão expiradas, por favor redefina sua senha");
            }

            var userPrincipal = new UserPrincipal(user, userCredential);

            validAccount.accept(userPrincipal);

            if(encoder.matches(apiAuthentication.getPassword(), userCredential.getPassword())) {
                return ApiAuthentication.authenticated(user, userPrincipal.getAuthorities());
            } else throw new ApiException("Credenciais inválidas. E-mail e/ou senha incorretos. Tente novamente");
        } throw new ApiException("Não foi possível autenticar o usuário");
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return ApiAuthentication.class.isAssignableFrom(authentication);
    }
}
