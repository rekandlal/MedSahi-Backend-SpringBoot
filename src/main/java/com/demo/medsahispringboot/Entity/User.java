//package com.demo.medsahispringboot.Entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "users")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class User {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String name;
//
//    @Column(unique = true)
//    private String email;
//
//    private String password;
//
//    @Enumerated(EnumType.STRING)
//    private Role role; // USER, ADMIN, PHARMACIST
//
//    private LocalDateTime createdAt;
//
//    private LocalDateTime updatedAt;
//
//    // Relationships
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<Order> orders;
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<Return> returns;
//}
