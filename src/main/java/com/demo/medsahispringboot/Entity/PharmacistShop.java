package com.demo.medsahispringboot.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="pharmacist_shops")
public class PharmacistShop {

@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

@ManyToOne
    @JoinColumn(name="pharmacist_id")
    private GenericMedicine genericMedicine;

private int quantityAvailable;
private double price;
}
