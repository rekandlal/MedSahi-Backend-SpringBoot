package com.demo.medsahispringboot.Controller;

import com.demo.medsahispringboot.Configuration.JwtUtil;
import com.demo.medsahispringboot.Dto.AuthRequest;

import com.demo.medsahispringboot.Dto.RegisterRequest;

import com.demo.medsahispringboot.Entity.Admin;
import com.demo.medsahispringboot.Entity.Pharmacist;
import com.demo.medsahispringboot.Entity.Role;
import com.demo.medsahispringboot.Entity.User;
import com.demo.medsahispringboot.Repository.AdminRepository;
import com.demo.medsahispringboot.Repository.PharmacistRepository;
import com.demo.medsahispringboot.Repository.RoleRepository;
import com.demo.medsahispringboot.Repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepo;
    private final AdminRepository adminRepo;
    private final PharmacistRepository pharmacistRepo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    private final Map<String, Long> tokenBlacklist = new HashMap<>();
    private final RoleRepository roleRepository;

    public AuthController(UserRepository userRepo, AdminRepository adminRepo,
                          PharmacistRepository pharmacistRepo, PasswordEncoder encoder,
                          AuthenticationManager authManager, JwtUtil jwtUtil, RoleRepository roleRepository) {
        this.userRepo = userRepo;
        this.adminRepo = adminRepo;
        this.pharmacistRepo = pharmacistRepo;
        this.encoder = encoder;
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.roleRepository = roleRepository;
    }

    // -------- REGISTER --------
    @PostMapping("/register/user")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest req){
        if(userRepo.existsByEmail(req.getEmail())) return ResponseEntity.badRequest().body(Map.of("error","Email exists"));
        User u = new User();
        u.setEmail(req.getEmail());
        u.setFullName(req.getFullName());
        u.setPhone(req.getPhone());
        u.setPassword(encoder.encode(req.getPassword()));

        // Default role USER
        Role role = roleRepository.findByName("USER")
                .orElseGet(() -> roleRepository.save(new Role(null, "USER")));
        u.getRoles().add(role);


        userRepo.save(u);
        return ResponseEntity.ok(Map.of("message","User registered successfully"));
    }

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterRequest req){
        if(adminRepo.existsByEmail(req.getEmail())) return ResponseEntity.badRequest().body(Map.of("error","Email exists"));
        Admin a = new Admin();
        a.setEmail(req.getEmail());
        a.setFullName(req.getFullName());
        a.setPhone(req.getPhone());
        a.setPassword(encoder.encode(req.getPassword()));

        // Default role ADMIN
        Role role = roleRepository.findByName("ADMIN")
                .orElseGet(() -> roleRepository.save(new Role(null, "ADMIN")));
        a.getRoles().add(role);

        adminRepo.save(a);
        return ResponseEntity.ok(Map.of("message","Admin registered successfully"));
    }

    @PostMapping("/register/pharmacist")
    public ResponseEntity<?> registerPharmacist(@RequestBody RegisterRequest req){
        if(pharmacistRepo.existsByEmail(req.getEmail())) return ResponseEntity.badRequest().body(Map.of("error","Email exists"));
        Pharmacist p = new Pharmacist();
        p.setEmail(req.getEmail());
        p.setFullName(req.getFullName());
        p.setPhone(req.getPhone());
        p.setLicenseNumber(req.getLicenseNumber());
        p.setPassword(encoder.encode(req.getPassword()));

        // Default role PHARMACIST
        Role role = roleRepository.findByName("PHARMACIST")
                .orElseGet(() -> roleRepository.save(new Role(null, "PHARMACIST")));
        p.getRoles().add(role);

        pharmacistRepo.save(p);
        return ResponseEntity.ok(Map.of("message","Pharmacist registered successfully"));
    }

    // -------- LOGIN --------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req){
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(Map.of("error","Invalid credentials"));
        }

        String role = "";
        String fullName = "";

        // check which table the user exists in
        if(userRepo.findByEmail(req.getEmail()).isPresent()){
            role = "USER";
            fullName = userRepo.findByEmail(req.getEmail()).get().getFullName();
        } else if(adminRepo.findByEmail(req.getEmail()).isPresent()){
            role = "ADMIN";
            fullName = adminRepo.findByEmail(req.getEmail()).get().getFullName();
        } else if(pharmacistRepo.findByEmail(req.getEmail()).isPresent()){
            role = "PHARMACIST";
            fullName = pharmacistRepo.findByEmail(req.getEmail()).get().getFullName();
        } else return ResponseEntity.status(401).body(Map.of("error","User not found"));

        // ✅ Pass the role as list to JWT
        String token = jwtUtil.generateToken(req.getEmail(), List.of(role));

        return ResponseEntity.ok(Map.of(
                "token", token,
                "type", "Bearer",
                "email", req.getEmail(),
                "fullName", fullName,
                "roles", List.of(role)
        ));
    }


    // -------- LOGOUT --------
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(name = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(Map.of("error", "No token provided"));
        }

        String token = authHeader.substring(7);
        try {
            // JWT token expiry check
            Date exp = jwtUtil.extractExpiration(token);
            long ttl = exp.getTime() - System.currentTimeMillis();

            if (ttl <= 0) {
                return ResponseEntity.ok(Map.of("message", "Token already expired"));
            }

            // Token ko blacklist me add karo
            tokenBlacklist.put(token, System.currentTimeMillis() + ttl);

            return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid token"));
        }
    }



    // -------- CURRENT USER --------
    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader(name="Authorization") String authHeader){
        if(authHeader == null || !authHeader.startsWith("Bearer ")) return ResponseEntity.status(401).body(Map.of("error","No token"));
        String token = authHeader.substring(7);

        Long expiry = tokenBlacklist.get(token);
        if(expiry != null && System.currentTimeMillis() < expiry){
            return ResponseEntity.status(401).body(Map.of("error","Token blacklisted"));
        }

        try {
            String email = jwtUtil.extractUsername(token);
            List<String> roles = jwtUtil.extractRoles(token);
            return ResponseEntity.ok(Map.of(
                    "email", email,
                    "roles", roles
            ));
        } catch(Exception e){
            return ResponseEntity.status(401).body(Map.of("error","Invalid token"));
        }
    }
}
