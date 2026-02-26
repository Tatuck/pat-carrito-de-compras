package edu.comillas.icai.gitt.pat.spring.practcarritos;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class HandlerGlobalErrores {

    // Validación fallida en el cuerpo de la petición (@Valid en el controlador)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("errors", errors);

        return response;
    }

    // Validación fallida dentro de una transacción JPA (por ejemplo, al guardar una entidad con @Valid)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TransactionSystemException.class)
    public Map<String, Object> handleTransactionException(TransactionSystemException ex) {
        Map<String, Object> response = new HashMap<>();

        Throwable cause = ex.getRootCause();
        if (cause instanceof ConstraintViolationException cve) {
            Map<String, String> errors = new HashMap<>();
            cve.getConstraintViolations().forEach(violation ->
                errors.put(violation.getPropertyPath().toString(), violation.getMessage())
            );
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("errors", errors);
        } else {
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("errors", ex.getMessage());
        }

        return response;
    }
}
