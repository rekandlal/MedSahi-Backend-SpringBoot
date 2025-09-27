package com.demo.medsahispringboot.Service.Impl;

import com.demo.medsahispringboot.Dto.LoginRequest;
import com.demo.medsahispringboot.Dto.LoginResponse;
import com.demo.medsahispringboot.Dto.RegisterRequest;
import com.demo.medsahispringboot.Entity.User;
import com.demo.medsahispringboot.Repository.UserRepository;
import com.demo.medsahispringboot.Util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public String register(RegisterRequest request){
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email Already Exists");
        }

        User user=User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .address(request.getAddress())
                .phone(request.getPhone())
                .role("USER")
                .build();

        UserRepository.save(user);
        return "User registerd Sucessfully";
    }

    @Override
    public LoginResponse login(LoginRequest request){
        User user=userRepository.findByEmail(request.getEmail())
                .orElseThrow(()->new RuntimeException("Invalid Email Or Password"));

        if(!passwordEncoder.matches(request.getPassword(),user.getPassword())) {
            throw new RuntimeException("Invalid Password");
        }

        String token =jwtUtil.generateToken(user.getEmail());
        return LoginResponse.builder()
                .token(token)
                .role(user.getRole())
                .build();
    }
}
