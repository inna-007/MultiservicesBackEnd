package com.multiservice.AppService.exceptions;

import com.multiservice.AppService.dto.ApiResponse;
import com.multiservice.AppService.exceptions.Categories.CategoryNotFoundException;
import com.multiservice.AppService.exceptions.admin.UserDeletionSuccessException;
import com.multiservice.AppService.exceptions.auth.*;
import com.multiservice.AppService.exceptions.email.SendingEmailException;
import com.multiservice.AppService.exceptions.service.ServiceNotFoundException;
import com.multiservice.AppService.exceptions.update.ProfileNotFoundException;
import com.multiservice.AppService.exceptions.update.UserNotAuthenticatedException;
import com.multiservice.AppService.exceptions.reservation.*;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Existing exception handlers

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        ApiResponse response = new ApiResponse("User already exists", ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // ... other existing exception handlers ...

    // Reservation exception handlers

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<ApiResponse> handleReservationNotFound(ReservationNotFoundException ex) {
        ApiResponse response = new ApiResponse("Reservation Not Found", ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProviderNotFoundException.class)
    public ResponseEntity<ApiResponse> handleProviderNotFound(ProviderNotFoundException ex) {
        ApiResponse response = new ApiResponse("Provider Not Found", ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServiceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleServiceNotFound(ServiceNotFoundException ex) {
        ApiResponse response = new ApiResponse("Service Not Found", ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ReservationConflictException.class)
    public ResponseEntity<ApiResponse> handleReservationConflict(ReservationConflictException ex) {
        ApiResponse response = new ApiResponse("Reservation Conflict", ex.getMessage(), HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    // Generic exception handlers

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // Other existing exception handlers

    @ExceptionHandler(UserDeletionSuccessException.class)
    public ResponseEntity<String> handleUserDeletionSuccess(UserDeletionSuccessException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.OK);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDenied(AccessDeniedException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ProfileNotFoundException.class)
    public ResponseEntity<ApiResponse> handleProfileNotFound(ProfileNotFoundException ex) {
        ApiResponse response = new ApiResponse("Profile Not Found", ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotAuthenticatedException.class)
    public ResponseEntity<ApiResponse> handleUserNotAuthenticated(UserNotAuthenticatedException ex) {
        ApiResponse response = new ApiResponse("Authentication Error", ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException e) {
        ApiResponse response = new ApiResponse("Internal Error", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


}
