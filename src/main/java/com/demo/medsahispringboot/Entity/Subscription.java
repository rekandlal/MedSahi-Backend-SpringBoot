package com.demo.medsahispringboot.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "pharmacist_id")
    private User pharmacist;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean isActive;
}

