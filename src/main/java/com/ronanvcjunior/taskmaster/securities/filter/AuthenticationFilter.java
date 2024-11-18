package com.ronanvcjunior.taskmaster.securities.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ronanvcjunior.taskmaster.domains.ApiAuthentication;
import com.ronanvcjunior.taskmaster.dtos.Response;
import com.ronanvcjunior.taskmaster.dtos.User;
import com.ronanvcjunior.taskmaster.dtos.requests.UserLoginRequest;
import com.ronanvcjunior.taskmaster.services.JwtService;
import com.ronanvcjunior.taskmaster.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import static com.fasterxml.jackson.core.JsonParser.Feature.AUTO_CLOSE_SOURCE;
import static com.ronanvcjunior.taskmaster.constants.SecurityConstants.PATH_LOGIN;
import static com.ronanvcjunior.taskmaster.domains.ApiAuthentication.*;
import static com.ronanvcjunior.taskmaster.enums.LoginType.LOGIN_ATTEMPT;
import static com.ronanvcjunior.taskmaster.enums.LoginType.LOGIN_SUCCESS;
import static com.ronanvcjunior.taskmaster.enums.TokenType.ACCESS;
import static com.ronanvcjunior.taskmaster.enums.TokenType.REFRESH;
import static com.ronanvcjunior.taskmaster.utils.RequestUtils.getResponse;
import static com.ronanvcjunior.taskmaster.utils.RequestUtils.handleErrorResponse;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Slf4j
public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService, JwtService jwtService) {
        super(new AntPathRequestMatcher(PATH_LOGIN, POST.name()), authenticationManager);
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        try {
            UserLoginRequest user = new ObjectMapper().configure(AUTO_CLOSE_SOURCE, true).readValue(request.getInputStream(), UserLoginRequest.class);

            this.userService.updateLoginAttempt(user.email(), LOGIN_ATTEMPT);

            ApiAuthentication authentication = unauthenticated(user.email(), user.password());

            return getAuthenticationManager().authenticate(authentication);
        } catch (Exception e) {
            log.error(e.getMessage());
            handleErrorResponse(request, response, e);
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();

        userService.updateLoginAttempt(user.email(), LOGIN_SUCCESS);

        Response httpResponse = this.sendResponse(request, response, user);

        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(OK.value());

        OutputStream out = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, httpResponse);

        out.flush();
    }

    private Response sendResponse(HttpServletRequest request, HttpServletResponse response, User user) {
        jwtService.addCookie(response, user, ACCESS);
        jwtService.addCookie(response, user, REFRESH);

        return getResponse(request, Map.of("user", user), "Login Success", OK);
    }

}
