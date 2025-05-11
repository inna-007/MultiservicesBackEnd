package com.multiservice.AppService.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class ErrorResponse {
    // génère un constructeur avec tous les champs en paramètre
        private int status;
        private String error;
        private String message;
    }

