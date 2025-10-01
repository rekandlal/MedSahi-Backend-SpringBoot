package com.demo.medsahispringboot.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "doctor_consultations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorConsultation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String issueDescription;
    private String status = "REQUESTED"; // REQUESTED, COMPLETED, CANCELLED

    @ManyToOne
    private User user;

    @ManyToOne
    private Doctor doctor;   // 🔹 Doctor assign ho gaya

    private LocalDateTime requestedAt = LocalDateTime.now();
}
