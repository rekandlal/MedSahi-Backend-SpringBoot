package com.demo.medsahispringboot.Dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String role; // USER / ADMIN / PHARMACIST
    private String licenseNumber;
}
