package com.demo.medsahispringboot.Dto;

import lombok.*;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor @Builder
public class LoginResponse {
    private String token;
    private String role;
}
