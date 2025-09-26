package com.demo.medsahispringboot.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String address;
    private String phone;
    private String role; // USER, ADMIN, PHARMACIST

    @ManyToOne
    @JoinColumn(name = "generic_medicine_id")
    private List<Order> orders = new ArrayList<>();


    @OneToMany(mappedBy = "brandedMedicine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicineDonation> donations =  new ArrayList<>();


}
