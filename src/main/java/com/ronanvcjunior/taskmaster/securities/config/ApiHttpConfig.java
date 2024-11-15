package com.ronanvcjunior.taskmaster.securities.config;

import com.ronanvcjunior.taskmaster.securities.filter.AuthenticationFilter;
import com.ronanvcjunior.taskmaster.securities.filter.AuthorizationFilter;
import com.ronanvcjunior.taskmaster.services.JwtService;
import com.ronanvcjunior.taskmaster.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApiHttpConfig extends AbstractHttpConfigurer<ApiHttpConfig, HttpSecurity> {
    private final AuthorizationFilter authorizationFilter;
    private final ApiAuthenticationProvider apiAuthenticationProvider;
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Override
    public void init(HttpSecurity http) throws Exception {
        http.authenticationProvider(apiAuthenticationProvider);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(new AuthenticationFilter(authenticationConfiguration.getAuthenticationManager(), userService, jwtService), UsernamePasswordAuthenticationFilter.class);
    }
}