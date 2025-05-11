package com.multiservice.AppService.exceptions.reservation;

public class ProviderNotFoundException extends RuntimeException {
    public ProviderNotFoundException(Long providerId) {
        super("Aucun fournisseur trouvé avec l'id : " + providerId);
    }
}
