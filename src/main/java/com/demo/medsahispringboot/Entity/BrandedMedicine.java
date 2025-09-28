//package com.demo.medsahispringboot.Entity;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;
//import lombok.*;
//
//@Entity
//@Table(name = "branded_medicines")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class BrandedMedicine {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String name;
//    private String ingredient;
//    private String dosage;
//    private String manufacturer;
//    private double MRP;
//    private double finalPrice;
//    private String form;
//
//    // One branded medicine → multiple generic alternatives
//    @OneToMany(mappedBy = "brandedMedicine", cascade = CascadeType.ALL)
//    private List<GenericMedicine> alternatives;
//}

