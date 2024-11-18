package com.ronanvcjunior.taskmaster.securities.filter;

import com.ronanvcjunior.taskmaster.domains.ApiAuthentication;
import com.ronanvcjunior.taskmaster.domains.RequestContext;
import com.ronanvcjunior.taskmaster.dtos.Token;
import com.ronanvcjunior.taskmaster.dtos.TokenData;
import com.ronanvcjunior.taskmaster.dtos.User;
import com.ronanvcjunior.taskmaster.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;


import static com.ronanvcjunior.taskmaster.constants.SecurityConstants.PUBLIC_ROUTES;
import static com.ronanvcjunior.taskmaster.enums.TokenType.ACCESS;
import static com.ronanvcjunior.taskmaster.enums.TokenType.REFRESH;
import static com.ronanvcjunior.taskmaster.utils.RequestUtils.handleErrorResponse;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            Optional<String> accessToken = jwtService.extractToken(request, ACCESS.getValue());

            if(accessToken.isPresent() && jwtService.getTokenData(accessToken.get(), TokenData::valid)) {
                SecurityContextHolder.getContext().setAuthentication(getAuthentication(accessToken.get(), request));

                RequestContext.setUserId(jwtService.getTokenData(accessToken.get(), TokenData::user).id());
            }else {
                Optional<String> refreshToken = jwtService.extractToken(request, REFRESH.getValue());

                if(refreshToken.isPresent() && jwtService.getTokenData(refreshToken.get(), TokenData::valid)) {
                    User user = jwtService.getTokenData(refreshToken.get(), TokenData::user);

                    SecurityContextHolder.getContext().setAuthentication(getAuthentication(jwtService.createToken(user, Token::access), request));

                    jwtService.addCookie(response, user, ACCESS);

                    RequestContext.setUserId(user.id());
                } else {
                    SecurityContextHolder.clearContext();
                }
            }
            filterChain.doFilter(request, response);
        }catch (Exception exception) {
            log.error(exception.getMessage());
            handleErrorResponse(request, response, exception);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        var shouldNotFilter = request.getMethod().equalsIgnoreCase(HttpMethod.OPTIONS.name()) || Arrays.asList(PUBLIC_ROUTES).contains(request.getRequestURI());
        if (shouldNotFilter) {
            RequestContext.setUserId(0L);
        }
        return shouldNotFilter;
    }

    private Authentication getAuthentication(String token, HttpServletRequest request) {
        var authentication = ApiAuthentication.authenticated(jwtService.getTokenData(token, TokenData::user), jwtService.getTokenData(token, TokenData::authorities));
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authentication;
    }
}