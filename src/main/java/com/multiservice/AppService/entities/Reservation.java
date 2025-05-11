package com.multiservice.AppService.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.Date;

@Entity
@AllArgsConstructor
@Table(name = "reservations")
@NoArgsConstructor
@Getter
@Setter
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status = ReservationStatus.PENDING;

    @Column(nullable = false)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    @JsonManagedReference
    private Services service;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(nullable = false)
    private Date createdAt = new Date();

    @Column
    private String notes;
}
