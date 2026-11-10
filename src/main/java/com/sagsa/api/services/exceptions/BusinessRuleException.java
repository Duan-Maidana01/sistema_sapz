package com.sagsa.api.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Regra de negócio violada (422)
@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class BusinessRuleException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BusinessRuleException(String message) {
        super(message);
    }
}
