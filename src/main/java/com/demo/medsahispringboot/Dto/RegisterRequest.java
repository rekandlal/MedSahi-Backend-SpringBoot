package com.demo.medsahispringboot.Dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private  String address;
    private String phone;
}
