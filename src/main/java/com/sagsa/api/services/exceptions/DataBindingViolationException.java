package com.sagsa.api.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Conflito de vínculo de dados (409)
@ResponseStatus(value = HttpStatus.CONFLICT)
public class DataBindingViolationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DataBindingViolationException(String message) {
        super(message);
    }
}
