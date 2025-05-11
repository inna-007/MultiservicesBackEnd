package com.multiservice.AppService.repositories;

import com.multiservice.AppService.entities.Reservation;
import com.multiservice.AppService.entities.Services;
import com.multiservice.AppService.entities.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByServiceProvider_Id(Long providerId);

    List<Reservation> findByServiceIn(List<Services> services);

    boolean existsByServiceAndDate(Services service, LocalDate date);

    List<Reservation> findByClientId(Long clientId);

    Optional<Reservation> findByIdAndClientId(Long id, Long clientId);

    @Query("SELECT r FROM Reservation r WHERE r.service.provider.id = :providerId")
    List<Reservation> findByProviderId(@Param("providerId") Long providerId);

    @Query("SELECT r FROM Reservation r WHERE r.service.provider.id = :providerId AND r.status IN :statuses")
    List<Reservation> findByProviderIdAndStatusIn(@Param("providerId") Long providerId, @Param("statuses") List<com.multiservice.AppService.entities.ReservationStatus> statuses);

}
