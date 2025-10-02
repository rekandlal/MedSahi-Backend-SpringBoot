package com.demo.medsahispringboot.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "medicine")
    @JsonManagedReference   // 👈 Pair with @JsonBackReference
    private List<GenericMedicine> generics;


    @Column(nullable = false) private String addedBy; // Pharmacist email/username who added this
}

