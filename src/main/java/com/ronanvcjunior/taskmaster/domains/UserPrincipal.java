package com.ronanvcjunior.taskmaster.domains;

import com.ronanvcjunior.taskmaster.dtos.User;
import com.ronanvcjunior.taskmaster.entities.CredentialEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import static org.springframework.security.core.authority.AuthorityUtils.commaSeparatedStringToAuthorityList;

@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {

    @Getter
    private final User user;

    private final CredentialEntity credentialEntity;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return commaSeparatedStringToAuthorityList(user.authorities());
    }

    @Override
    public String getPassword() {
        return credentialEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return user.email();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.accountUnexpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.accountUnlocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.credentialUnexpired();
    }

    @Override
    public boolean isEnabled() {
        return user.enabled();
    }
}