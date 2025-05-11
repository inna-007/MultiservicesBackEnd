package com.multiservice.AppService.controller;

import com.multiservice.AppService.dto.CreateReservationDTO;
import com.multiservice.AppService.entities.Provider;
import com.multiservice.AppService.entities.Reservation;
import com.multiservice.AppService.entities.ReservationStatus;
import com.multiservice.AppService.entities.Client;
import com.multiservice.AppService.entities.User;
import com.multiservice.AppService.entities.Services;
import com.multiservice.AppService.repositories.ClientRepository;
import com.multiservice.AppService.repositories.ProviderRepository;
import com.multiservice.AppService.repositories.UserRepository;
import com.multiservice.AppService.repositories.ServiceRepository;
import com.multiservice.AppService.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/reservations")
@CrossOrigin("*")
public class ReservationController {

    @Autowired private ReservationService reservationService;
    @Autowired private ClientRepository clientRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ProviderRepository providerRepository;
    @Autowired private ServiceRepository serviceRepository;

    // ---------------------- CLIENT ----------------------

    @PostMapping("/client")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public Reservation createReservation(@RequestBody CreateReservationDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Client client = clientRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Client not found"));
        
        Services service = serviceRepository.findById(dto.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found"));

        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setService(service);
        reservation.setDate(dto.getDate());
        reservation.setNotes(dto.getNotes());
        reservation.setStatus(ReservationStatus.PENDING);

        return reservationService.createReservation(reservation);
    }

    @GetMapping("/client")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public List<Reservation> getAllReservationsForClient() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Client client = clientRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Client not found"));
        return reservationService.getReservationsForClient(client.getId());
    }

    @GetMapping("/client/{reservationId}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public Reservation getReservationForClientById(@PathVariable Long reservationId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Client client = clientRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Client not found"));
        return reservationService.getReservationForClient(reservationId, client.getId());
    }

    @PostMapping("/client/{reservationId}/cancel")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public Reservation cancelReservation(@PathVariable Long reservationId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Client client = clientRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Client not found"));
        Reservation reservation = reservationService.getReservationForClient(reservationId, client.getId());
        return reservationService.updateReservationStatus(reservationId, ReservationStatus.CANCELLED);
    }

    // ---------------------- PROVIDER ----------------------

    @GetMapping("/provider")
    @PreAuthorize("hasRole('ROLE_PROVIDER')")
    public Map<String, Object> getReservationsForProvider() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Provider provider = providerRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Provider not found"));
        List<Reservation> reservations = reservationService.getReservationsForProvider(provider.getId());
        Map<String, Object> response = new HashMap<>();
        response.put("data", reservations);
        response.put("count", reservations.size());
        response.put("providerId", provider.getId());
        return response;
    }

    @PostMapping("/provider/{reservationId}/accept")
    @PreAuthorize("hasRole('ROLE_PROVIDER')")
    public Reservation acceptReservation(@PathVariable Long reservationId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Provider provider = providerRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Provider not found"));
        Reservation reservation = reservationService.getReservationById(reservationId);
        if (!reservation.getService().getProvider().getId().equals(provider.getId())) {
            throw new RuntimeException("Not authorized to accept this reservation");
        }
        return reservationService.updateReservationStatus(reservationId, ReservationStatus.ACCEPTED);
    }

    @PostMapping("/provider/{reservationId}/reject")
    @PreAuthorize("hasRole('ROLE_PROVIDER')")
    public Reservation rejectReservation(@PathVariable Long reservationId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Provider provider = providerRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Provider not found"));
        Reservation reservation = reservationService.getReservationById(reservationId);
        if (!reservation.getService().getProvider().getId().equals(provider.getId())) {
            throw new RuntimeException("Not authorized to reject this reservation");
        }
        return reservationService.updateReservationStatus(reservationId, ReservationStatus.REJECTED);
    }

    @GetMapping("/provider/all-status")
    @PreAuthorize("hasRole('ROLE_PROVIDER')")
    public Map<String, Object> getAllStatusReservationsForProvider() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Provider provider = providerRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Provider not found"));
        List<Reservation> reservations = reservationService.getReservationsForProviderWithStatuses(
            provider.getId(),
            List.of(ReservationStatus.ACCEPTED, ReservationStatus.PENDING, ReservationStatus.REJECTED)
        );
        Map<String, Object> response = new HashMap<>();
        response.put("data", reservations);
        response.put("count", reservations.size());
        response.put("providerId", provider.getId());
        return response;
    }

    // ---------------------- COMMON (admin or for all) ----------------------

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT', 'ROLE_PROVIDER')")
    public Reservation getReservationById(@PathVariable Long id) {
        return reservationService.getReservationById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Reservation updateReservation(@PathVariable Long id,
                                         @RequestBody Reservation updatedReservation) {
        return reservationService.updateReservation(id, updatedReservation);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
    }
}
