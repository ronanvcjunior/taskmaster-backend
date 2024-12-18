package com.ronanvcjunior.taskmaster.services;

import com.ronanvcjunior.taskmaster.dtos.Token;
import com.ronanvcjunior.taskmaster.dtos.TokenData;
import com.ronanvcjunior.taskmaster.dtos.User;
import com.ronanvcjunior.taskmaster.enums.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;
import java.util.function.Function;

public interface JwtService {
    String createToken(User user, Function<Token, String> tokenFunction);
    Optional<String> extractToken(HttpServletRequest request, String cookieName);
    void addCookie(HttpServletResponse response, User user, TokenType type);
    <T> T getTokenData(String token, Function<TokenData, T> tokenFunction);
    void removeCookie(HttpServletRequest request, HttpServletResponse response, String cookieName);
}