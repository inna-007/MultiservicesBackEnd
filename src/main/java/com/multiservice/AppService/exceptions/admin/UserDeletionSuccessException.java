package com.multiservice.AppService.exceptions.admin;

public class UserDeletionSuccessException extends RuntimeException {
    public UserDeletionSuccessException(String message) {
        super(message);
    }
}
