package com.demo.medsahispringboot.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "lab_tests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LabTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String testName;
    private String status = "REQUESTED"; // REQUESTED, COMPLETED, CANCELLED

    @ManyToOne
    private User user;

    private LocalDateTime requestedAt = LocalDateTime.now();
}

