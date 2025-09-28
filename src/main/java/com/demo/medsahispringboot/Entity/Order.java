package com.demo.medsahispringboot.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User who placed the order
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Ordered medicine (generic or branded)
    @ManyToOne
    @JoinColumn(name = "medicine_id")
    private GenericMedicine genericMedicine; // You can extend for branded if needed

    private int quantity;
    private double totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // PENDING, FULFILLED, CANCELLED

    private LocalDateTime orderDate;
}

