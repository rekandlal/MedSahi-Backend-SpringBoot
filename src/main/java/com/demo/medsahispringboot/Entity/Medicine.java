package com.demo.medsahispringboot.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "medicines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brandedName;
    private String ingredient;
    private String dosage;
    private String manufacturer;
    private Double mrp;
    private Double finalPrice;
    private String form;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id") // FK in GenericMedicine table
    private List<GenericMedicine> generics;
}

