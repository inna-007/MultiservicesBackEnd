package com.multiservice.AppService.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "providers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Provider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String idCardFrontPath;

    @Column(nullable = false)
    private String idCardBackPath;

    @Column(nullable = false)
    private String status; // PENDING, APPROVED, REJECTED

    @Column(length = 20)
    private String cin;

    private Boolean disponibilites = false;

    private Boolean verificationCnie = false;
}
