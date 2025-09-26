package com.demo.medsahispringboot.Entity;

import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Enabled
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder

public class GenericMedicine {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String genericName;
    private String therapeuticUse;
    private String sideEffects;

    //One Generic Medicine Can Have Many BrandedMedicine
    @OneToMany(mappedBy ="genericMedicine",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Brandedmedicine> brandedMedicines=new ArrayList<>();
}
