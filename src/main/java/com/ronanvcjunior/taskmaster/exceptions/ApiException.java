package com.ronanvcjunior.taskmaster.exceptions;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }

    public ApiException() {
        super("Ocorreu um erro na API");
    }
}
