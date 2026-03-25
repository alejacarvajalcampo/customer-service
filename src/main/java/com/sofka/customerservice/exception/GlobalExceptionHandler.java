package com.sofka.customerservice.exception;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClienteNotFoundException.class)
    public ResponseEntity<ApiError> handleClienteNotFound(ClienteNotFoundException exception) {
        return buildResponse(HttpStatus.NOT_FOUND, "CLIENTE_NOT_FOUND", exception.getMessage(), List.of());
    }

    @ExceptionHandler(DuplicateClienteException.class)
    public ResponseEntity<ApiError> handleDuplicateCliente(DuplicateClienteException exception) {
        return buildResponse(HttpStatus.CONFLICT, "CLIENTE_DUPLICATE", exception.getMessage(), List.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationError(MethodArgumentNotValidException exception) {
        List<String> details = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatFieldError)
                .toList();

        return buildResponse(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "La solicitud contiene datos inválidos", details);
    }

    private String formatFieldError(FieldError fieldError) {
        return fieldError.getField() + ": " + fieldError.getDefaultMessage();
    }

    private ResponseEntity<ApiError> buildResponse(HttpStatus status, String code, String message, List<String> details) {
        ApiError error = new ApiError(
                LocalDateTime.now(),
                status.value(),
                code,
                status.getReasonPhrase(),
                message,
                details
        );
        return ResponseEntity.status(status).body(error);
    }
}
