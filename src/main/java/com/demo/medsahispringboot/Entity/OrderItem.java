package com.demo.medsahispringboot.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;
    private Double price;

    //Many Items can Belong to one order
    @ManyToOne
    @JoinColumn(name="order_id",nullable = false)
    private Order order;

    //Each OrderItem Is Linked to one BrandedMedicine
    @ManyToOne
    @JoinColumn(name="branded_medicine_id",nullable=false)
    private BrandedMedicine brandedMedicine;
}
