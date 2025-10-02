package com.demo.medsahispringboot.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "generic_medicines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenericMedicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String ingredient;
    private String dosage;
    private String manufacturer;
    private Double mrp;
    private Double finalPrice;
    private String form;

    @ManyToOne
    @JoinColumn(name = "medicine_id")
    @JsonBackReference
    private Medicine medicine;
}

