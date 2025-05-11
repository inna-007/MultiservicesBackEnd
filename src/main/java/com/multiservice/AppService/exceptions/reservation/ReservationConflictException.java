package com.multiservice.AppService.exceptions.reservation;

public class ReservationConflictException extends RuntimeException {
    public ReservationConflictException(String message) {
        super(message);
    }
}
