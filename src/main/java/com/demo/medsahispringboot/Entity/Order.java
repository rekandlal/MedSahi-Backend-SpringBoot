package com.demo.medsahispringboot.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Updated: Only generic medicines
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "order_generic_medicines",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "generic_medicine_id")
    )
    private List<GenericMedicine> genericMedicines;

    private Double totalAmount;
    private String status; // PLACED, DELIVERED, CANCELLED etc.
    private LocalDateTime orderTime;
}
