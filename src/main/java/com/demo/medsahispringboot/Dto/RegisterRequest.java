package com.demo.medsahispringboot.Dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String email;
    private String password;
    private String fullName;
    private String phone;
    // optional: role (USER/PHARMACIST/ADMIN) - default USER if not provided
    private String role;
}

