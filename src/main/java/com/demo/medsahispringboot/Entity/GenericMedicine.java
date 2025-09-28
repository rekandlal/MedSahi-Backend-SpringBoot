package com.demo.medsahispringboot.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "generic_medicines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenericMedicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String ingredient;
    private String dosage;
    private String manufacturer;
    private double MRP;
    private double finalPrice;
    private String form;

    // Many generics → one branded medicine
    @ManyToOne
    @JoinColumn(name = "branded_id")
    private BrandedMedicine brandedMedicine;
}

