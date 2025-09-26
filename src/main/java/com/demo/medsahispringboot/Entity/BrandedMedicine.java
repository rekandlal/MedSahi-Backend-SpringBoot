package com.demo.medsahispringboot.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class BrandedMedicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brandName;
    private String manufacturer;
    private Double price;
    private Integer stock;

    //Each Branded medicine is linked to a generic Medicine
    @ManyToOne
    @JoinColumn(name="generic_medicine_id", nullable = false)
    private GenericMedicine genericMedicine;

    @OneToMany(mappedBy = "brandedMedicine",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<OrderItem> orderItems=new ArrayList<>();
}
