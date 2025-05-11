package com.multiservice.AppService.services;

import com.multiservice.AppService.entities.Reservation;
import com.multiservice.AppService.entities.ReservationStatus;
import java.util.List;

public interface ReservationService {
    // existing:
    Reservation createReservation(Reservation reservation);
    List<Reservation> getAllReservations();
    Reservation getReservationById(Long id);
    Reservation updateReservation(Long id, Reservation updated);
    void deleteReservation(Long id);
    Reservation updateReservationStatus(Long id, ReservationStatus status);

    // new:
    List<Reservation> getReservationsForClient(Long clientId);
    Reservation getReservationForClient(Long reservationId, Long clientId);

    List<Reservation> getReservationsForProvider(Long providerId);
    List<Reservation> getReservationsForProviderWithStatuses(Long providerId, List<ReservationStatus> statuses);
}
