package com.multiservice.AppService.services;

import com.multiservice.AppService.entities.Reservation;
import com.multiservice.AppService.entities.ReservationStatus;
import com.multiservice.AppService.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public Reservation createReservation(Reservation reservation) {
        reservation.setStatus(ReservationStatus.PENDING);
        return reservationRepository.save(reservation);
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
    }

    @Override
    public Reservation updateReservation(Long id, Reservation updated) {
        Reservation existing = getReservationById(id);
        existing.setDate(updated.getDate());
        existing.setNotes(updated.getNotes());
        return reservationRepository.save(existing);
    }

    @Override
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    @Override
    public Reservation updateReservationStatus(Long id, ReservationStatus status) {
        Reservation r = getReservationById(id);
        r.setStatus(status);
        return reservationRepository.save(r);
    }

    // ---- new methods ----

    @Override
    public List<Reservation> getReservationsForClient(Long clientId) {
        return reservationRepository.findByClientId(clientId);
    }

    @Override
    public Reservation getReservationForClient(Long reservationId, Long clientId) {
        return reservationRepository.findByIdAndClientId(reservationId, clientId)
                .orElseThrow(() -> new RuntimeException("Reservation not found for this client"));
    }

    @Override
    public List<Reservation> getReservationsForProvider(Long providerId) {
        return reservationRepository.findByProviderId(providerId);
    }

    @Override
    public List<Reservation> getReservationsForProviderWithStatuses(Long providerId, List<ReservationStatus> statuses) {
        return reservationRepository.findByProviderIdAndStatusIn(providerId, statuses);
    }

}
