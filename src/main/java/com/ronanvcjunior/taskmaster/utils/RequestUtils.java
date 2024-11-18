package com.ronanvcjunior.taskmaster.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.ronanvcjunior.taskmaster.dtos.Response;
import com.ronanvcjunior.taskmaster.exceptions.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static java.time.ZonedDateTime.now;
import static java.util.Collections.emptyMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCauseMessage;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

public class RequestUtils {

    private static final BiConsumer<HttpServletResponse, Response> writeResponse = (httpServletResponse, response) -> {
        try {
            var outputStream = httpServletResponse.getOutputStream();

            new ObjectMapper().writeValue(outputStream, response);

            outputStream.flush();
        } catch (Exception exception) {
            throw new ApiException(exception.getMessage());
        }
    };

    private static final BiFunction<Exception, HttpStatus, String> errorReason = (e, httpStatus) -> {
        if(httpStatus.isSameCodeAs(FORBIDDEN)) {
            return "Você não tem permissão suficiente";
        }
        if(httpStatus.isSameCodeAs(UNAUTHORIZED)) {
            return "Você não está logado";
        }
        if(
                e instanceof DisabledException ||
                        e instanceof LockedException ||
                        e instanceof BadCredentialsException ||
                        e instanceof CredentialsExpiredException ||
                        e instanceof ApiException
        ) {
            return e.getMessage();
        }
        if(httpStatus.is5xxServerError()) {
            return "Ocorreu um erro interno no servidor";
        }
        else {
            return "Ocorreu um erro. Por favor, tente novamente.";
        }
    };

    public static Response getResponse(final HttpServletRequest request, final Map<?, ?> data, final String message, final HttpStatus status) {
        return new Response(
                now().toString(),
                status.value(),
                request.getRequestURI(),
                valueOf(status.value()),
                message,
                EMPTY,
                data
        );
    }

    public static Response handleErrorResponse(String message, String exception, HttpServletRequest request, HttpStatusCode status) {
        return new Response(now().toString(), status.value(), request.getRequestURI(), HttpStatus.valueOf(status.value()), message, exception, emptyMap());
    }

    public static void handleErrorResponse(final HttpServletRequest request, final HttpServletResponse response, final Exception e) {
        if(e instanceof AccessDeniedException) {
            Response apiResponse = getErrorResponse(request, response, e, FORBIDDEN);
            writeResponse.accept(response, apiResponse);
        }
        else if(e instanceof InsufficientAuthenticationException) {
            var apiResponse = getErrorResponse(request, response, e, UNAUTHORIZED);
            writeResponse.accept(response, apiResponse);
        }
        else if(e instanceof MismatchedInputException) {
            var apiResponse = getErrorResponse(request, response, e, BAD_REQUEST);
            writeResponse.accept(response, apiResponse);
        }
        else if (
                e instanceof DisabledException ||
                        e instanceof LockedException ||
                        e instanceof BadCredentialsException ||
                        e instanceof CredentialsExpiredException ||
                        e instanceof ApiException) {
            var apiResponse = getErrorResponse(request, response, e, BAD_REQUEST);
            writeResponse.accept(response, apiResponse);
        } else {
            Response apiResponse = getErrorResponse(request, response, e, INTERNAL_SERVER_ERROR);
            writeResponse.accept(response, apiResponse);
        }
    }

    private static Response getErrorResponse(HttpServletRequest request, HttpServletResponse response, Exception e, HttpStatus status) {
        response.setContentType(APPLICATION_JSON_VALUE);

        response.setStatus(status.value());

        return new Response(now().toString(), status.value(), request.getRequestURI(), HttpStatus.valueOf(status.value()), errorReason.apply(e, status), getRootCauseMessage(e), emptyMap());
    }
}
