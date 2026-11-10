package com.sagsa.api.exceptions; // Manipulador de exceções globais da API

import org.apache.commons.lang3.exception.ExceptionUtils; // Extração da stacktrace
import org.springframework.beans.factory.annotation.Value; // Leitura de propriedades
import org.springframework.dao.DataIntegrityViolationException; // Violações de integridade no MySQL
import org.springframework.http.HttpHeaders; // Cabeçalhos HTTP
import org.springframework.http.HttpStatus; // Códigos de status HTTP
import org.springframework.http.HttpStatusCode; // Código de status genérico
import org.springframework.http.ResponseEntity; // Resposta HTTP
import org.springframework.lang.NonNull; // Anotação de não nulo
import org.springframework.security.access.AccessDeniedException; // Acesso negado (Spring Security)
import org.springframework.security.core.AuthenticationException; // Falha de autenticação
import org.springframework.validation.FieldError; // Erro de campo do @Valid
import org.springframework.web.bind.MethodArgumentNotValidException; // Falha de @Valid no @RequestBody
import org.springframework.web.bind.annotation.ExceptionHandler; // Vincula exceções a métodos
import org.springframework.web.bind.annotation.ResponseStatus; // Status HTTP padrão
import org.springframework.web.bind.annotation.RestControllerAdvice; // Interceptador global
import org.springframework.web.context.request.WebRequest; // Detalhes da requisição
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler; // Base do Spring MVC

import jakarta.validation.ConstraintViolation; // Violação do Bean Validation
import jakarta.validation.ConstraintViolationException; // Exceção de validação de beans

import com.sagsa.api.services.exceptions.BusinessRuleException; // Regra de negócio violada
import com.sagsa.api.services.exceptions.DataBindingViolationException; // Vínculo de dados
import com.sagsa.api.services.exceptions.ObjectNotFoundException; // Recurso não encontrado

// Uniformiza o formato do JSON retornado em erros na API
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // Indica se a stacktrace completa deve ser exibida no JSON de erro
    @Value("${server.error.include-exception:false}")
    private boolean printStackTrace;

    // 422 - Erros de validação dos DTOs anotados com @Valid no @RequestBody
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException methodArgumentNotValidException,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Erro de validação. Verifique o campo 'errors' para mais detalhes."
        );
        for (FieldError fieldError : methodArgumentNotValidException.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
    }

    // 404 - Recurso não encontrado
    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleObjectNotFoundException(
            ObjectNotFoundException objectNotFoundException,
            WebRequest request) {
        return buildErrorResponse(objectNotFoundException, objectNotFoundException.getMessage(), HttpStatus.NOT_FOUND, request);
    }

    // 409 - Conflito de vínculo de dados (ex.: exclusão com registros relacionados, duplicidade)
    @ExceptionHandler(DataBindingViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleDataBindingViolationException(
            DataBindingViolationException dataBindingViolationException,
            WebRequest request) {
        return buildErrorResponse(dataBindingViolationException, dataBindingViolationException.getMessage(), HttpStatus.CONFLICT, request);
    }

    // 422 - Regra de negócio violada (ex.: datas inválidas, capacidade de outra UC)
    @ExceptionHandler(BusinessRuleException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<Object> handleBusinessRuleException(
            BusinessRuleException businessRuleException,
            WebRequest request) {
        return buildErrorResponse(businessRuleException, businessRuleException.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    // 409 - Violação de chave única / estrangeira no banco
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleDataIntegrityViolationException(
            DataIntegrityViolationException dataIntegrityViolationException,
            WebRequest request) {
        return buildErrorResponse(dataIntegrityViolationException,
                "Violação de integridade dos dados no banco (registro duplicado ou vinculado a outro).",
                HttpStatus.CONFLICT, request);
    }

    // 403 - Autorização negada pela regra de negócio
    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleAuthorizationException(
            AuthorizationException authorizationException,
            WebRequest request) {
        return buildErrorResponse(authorizationException, authorizationException.getMessage(), HttpStatus.FORBIDDEN, request);
    }

    // 403 - Acesso negado pelo Spring Security (@PreAuthorize)
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleAccessDeniedException(
            AccessDeniedException accessDeniedException,
            WebRequest request) {
        return buildErrorResponse(accessDeniedException,
                "Acesso negado. Você não possui permissão para acessar este recurso.",
                HttpStatus.FORBIDDEN, request);
    }

    // 401 - Falha de autenticação
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> handleAuthenticationException(
            AuthenticationException authenticationException,
            WebRequest request) {
        return buildErrorResponse(authenticationException,
                "Falha na autenticação. E-mail ou senha inválidos.",
                HttpStatus.UNAUTHORIZED, request);
    }

    // 422 - Violações de Bean Validation nas entidades (persistência)
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<Object> handleConstraintViolationException(
            ConstraintViolationException constraintViolationException,
            WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Erro de validação. Verifique o campo 'errors' para mais detalhes."
        );
        for (ConstraintViolation<?> violation : constraintViolationException.getConstraintViolations()) {
            errorResponse.addValidationError(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
    }

    // 500 - Qualquer outra exceção não tratada
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtException(
            Exception exception,
            WebRequest request) {
        return buildErrorResponse(exception, "Ocorreu um erro interno no servidor.", HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    // Monta a estrutura padronizada de erro HTTP
    private ResponseEntity<Object> buildErrorResponse(
            Exception exception,
            String message,
            HttpStatus httpStatus,
            WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), message);
        if (this.printStackTrace) {
            errorResponse.setStackTrace(ExceptionUtils.getStackTrace(exception));
        }
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}
