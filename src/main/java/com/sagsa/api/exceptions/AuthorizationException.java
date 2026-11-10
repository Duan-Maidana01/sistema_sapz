package com.sagsa.api.exceptions; // Exceções de autorização da API

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN) // 403 - usuário autenticado sem permissão para a ação
public class AuthorizationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
