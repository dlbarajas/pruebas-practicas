package com.parque.diversiones.backend.exceptions;

import com.parque.diversiones.backend.dto.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleClienteException(GeneralException ex) {
        HttpStatus status;
        switch (ex.getTipo()) {
            case NOT_FOUND:
                status = HttpStatus.NOT_FOUND;
                break;
            case INHABILITADO:
                status = HttpStatus.FORBIDDEN;
                break;
            case VALOR_NO_VALIDO:
                status = HttpStatus.BAD_REQUEST;
                break;
            case UNAUTHORIZED:
                status = HttpStatus.UNAUTHORIZED;
                break;
            default:
                status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return ResponseEntity.status(status)
                .body(new ApiResponseDto<>(ex.getMessage(), null, false));
    }


    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseDto<>("Ruta no encontrada: " + ex.getRequestURL(), null, false));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new ApiResponseDto<>("Método HTTP no permitido para esta ruta", null, false));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<Void>> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDto<>("Error interno del servidor: " + ex.getMessage(), null, false));
    }
}
