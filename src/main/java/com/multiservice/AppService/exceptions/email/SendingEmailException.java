package com.multiservice.AppService.exceptions.email;

public class SendingEmailException extends RuntimeException {
    public SendingEmailException(String message) {
        super(message);
    }
}
