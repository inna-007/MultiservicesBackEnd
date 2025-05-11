package com.multiservice.AppService.exceptions.reservation;

public class ReservationNotFoundException extends RuntimeException {
  public ReservationNotFoundException(Long id) {
    super("Réservation introuvable avec l'id : " + id);
  }}