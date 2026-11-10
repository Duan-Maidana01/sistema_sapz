package com.sagsa.api.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Recurso não encontrado (404)
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ObjectNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ObjectNotFoundException(String message) {
        super(message);
    }
}
