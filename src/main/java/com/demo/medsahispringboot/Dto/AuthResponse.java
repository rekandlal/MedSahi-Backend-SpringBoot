package com.demo.medsahispringboot.Dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String type;
    private String email;
    private String fullName;
    private List<String> roles;
}
