package com.multiservice.AppService.exceptions;

import com.multiservice.AppService.exceptions.service.ServiceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ServiceAccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleServiceAccessDenied(ServiceAccessDeniedException ex) {
        ErrorResponse err = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                "Accès refusé",
                ex.getMessage()
        );
        return new ResponseEntity<>(err, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ServiceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleServiceNotFound(ServiceNotFoundException ex) {
        ErrorResponse err = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Service introuvable",
                ex.getMessage()
        );
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }
}
