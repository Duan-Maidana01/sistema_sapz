package com.sagsa.api.exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

// Estrutura padronizada do JSON de erro retornado pela API
@Getter
@Setter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private final int status;
    private final String message;
    private String stackTrace;
    private List<ValidationError> errors;

    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class ValidationError {
        private final String field;
        private final String message;
    }

    public void addValidationError(String field, String message) {
        if (Objects.isNull(this.errors)) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(new ValidationError(field, message));
    }
}
