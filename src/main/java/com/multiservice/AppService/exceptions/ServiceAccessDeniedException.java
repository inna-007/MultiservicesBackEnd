package com.multiservice.AppService.exceptions;

public class ServiceAccessDeniedException extends RuntimeException {
  public ServiceAccessDeniedException(Long serviceId, Long providerId) {
    super("Le service " + serviceId + " n'appartient pas au provider " + providerId);
  }
}
