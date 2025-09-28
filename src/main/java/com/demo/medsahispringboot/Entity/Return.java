package com.demo.medsahispringboot.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="returns")
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Return {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long  id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private GenericMedicine genericMedicine;

    private int quantity;
    private String reason;

    @Enumerated(EnumType.STRING)
    private ReturnStatus status;
    private LocalDateTime returnDate;
}
