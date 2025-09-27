package com.demo.medsahispringboot.Service;

import com.demo.medsahispringboot.Dto.LoginRequest;
import com.demo.medsahispringboot.Dto.LoginResponse;
import com.demo.medsahispringboot.Dto.RegisterRequest;

public interface AuthService {
    String register(RegisterRequest request); // abstract method
    // how will implement this method ---> do via child(AuthServiceImpl)
    LoginResponse login(LoginRequest request);
}
