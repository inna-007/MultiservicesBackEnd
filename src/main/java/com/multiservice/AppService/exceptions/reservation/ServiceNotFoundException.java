package com.multiservice.AppService.exceptions.reservation;

public class ServiceNotFoundException extends RuntimeException {
    public ServiceNotFoundException(Long serviceId) {
        super("Aucun service trouvé avec l'id : " + serviceId);
    }
}
