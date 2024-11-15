package com.ronanvcjunior.taskmaster.utils;

import com.ronanvcjunior.taskmaster.dtos.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.Map;
import java.util.function.BiFunction;

import static java.time.ZonedDateTime.now;
import static java.util.Collections.emptyMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCauseMessage;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

public class RequestUtils {

    private static final BiFunction<Exception, HttpStatus, String> errorReason = (e, httpStatus) -> {
        if(httpStatus.isSameCodeAs(FORBIDDEN)) {
            return "Você não tem permissão suficiente";
        }
        if(httpStatus.isSameCodeAs(UNAUTHORIZED)) {
            return "Você não está logado";
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

    private static Response getErrorResponse(HttpServletRequest request, HttpServletResponse response, Exception e, HttpStatus status) {
        response.setContentType(APPLICATION_JSON_VALUE);

        response.setStatus(status.value());

        return new Response(now().toString(), status.value(), request.getRequestURI(), HttpStatus.valueOf(status.value()), errorReason.apply(e, status), getRootCauseMessage(e), emptyMap());
    }
}
