package com.multiservice.AppService.exceptions.service;

public class ServiceNotFoundException extends RuntimeException {
    public ServiceNotFoundException(Long id) {
        super("Service introuvable avec l'id : " + id);
    }
}