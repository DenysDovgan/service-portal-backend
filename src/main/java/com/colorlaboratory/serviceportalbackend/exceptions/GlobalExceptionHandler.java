package com.colorlaboratory.serviceportalbackend.exceptions;

import com.colorlaboratory.serviceportalbackend.exceptions.media.FileSizeLimitExceededException;
import com.colorlaboratory.serviceportalbackend.exceptions.media.UnsupportedMimeTypeException;
import com.colorlaboratory.serviceportalbackend.model.dto.api.responses.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
        log.error("Internal server error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Internal server error"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Invalid input data.");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiResponse.error("VALIDATION_ERROR", message)
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleEntityNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponse.error("Requested resource was not found.")
        );
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUsernameNotFound(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponse.error("User is not found.")
        );
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse<Object>> handleExpiredJwt(ExpiredJwtException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                ApiResponse.error("Your session has expired. Please log in again.")
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ApiResponse.error("Incorrect email or password.")
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                ApiResponse.error("You do not have permission to perform this action.")
        );
    }

    @ExceptionHandler(UnsupportedMimeTypeException.class)
    public ResponseEntity<ApiResponse<Object>> handleUnsupportedMimeType(UnsupportedMimeTypeException ex) {
        return ResponseEntity.badRequest().body(
                ApiResponse.error("UNSUPPORTED_MIME_TYPE", ex.getMessage())
        );
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity<ApiResponse<Object>> handleFileTooLarge(FileSizeLimitExceededException ex) {
        return ResponseEntity.badRequest().body(
                ApiResponse.error("FILE_TOO_LARGE", ex.getMessage())
        );
    }
}
