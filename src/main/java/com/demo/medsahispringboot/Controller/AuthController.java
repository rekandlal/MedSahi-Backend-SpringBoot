package com.demo.medsahispringboot.Controller;

import com.demo.medsahispringboot.Configuration.JwtUtil;
import com.demo.medsahispringboot.Dto.AuthRequest;
import com.demo.medsahispringboot.Dto.AuthResponse;
import com.demo.medsahispringboot.Dto.RegisterRequest;
import com.demo.medsahispringboot.Entity.Admin;
import com.demo.medsahispringboot.Entity.Pharmacist;
import com.demo.medsahispringboot.Entity.User;
import com.demo.medsahispringboot.Repository.AdminRepository;
import com.demo.medsahispringboot.Repository.PharmacistRepository;
import com.demo.medsahispringboot.Repository.UserRepository;
import com.demo.medsahispringboot.Service.AuthService;
import com.demo.medsahispringboot.Service.JwtUserDetailsService;
import com.demo.medsahispringboot.Service.TokenBlacklistService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final JwtUserDetailsService userDetailsService;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final PharmacistRepository pharmacistRepository;
    private final TokenBlacklistService blacklistService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          JwtUserDetailsService userDetailsService,
                          AuthService authService,
                          UserRepository userRepository,
                          AdminRepository adminRepository,
                          PharmacistRepository pharmacistRepository,
                          TokenBlacklistService blacklistService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.authService = authService;
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.pharmacistRepository = pharmacistRepository;
        this.blacklistService = blacklistService;
    }

    // ----------------- REGISTER -----------------
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        try {
            Object saved = authService.registerNewUser(req);
            return ResponseEntity.ok(Map.of(
                    "message", "registered",
                    "email", req.getEmail(),
                    "role", req.getRole()
            ));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    // ----------------- LOGIN -----------------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
            String token = jwtUtil.generateToken(userDetails.getUsername(),
                    userDetails.getAuthorities().stream()
                            .map(a -> a.getAuthority())
                            .toList()
            );

            String role = userDetails.getAuthorities().stream().findFirst().get().getAuthority().replace("ROLE_", "");

            // Fetch fullName from the correct table
            String fullName = "";
            if (role.equals("USER")) {
                User u = userRepository.findByEmail(request.getEmail()).get();
                fullName = u.getFullName();
            } else if (role.equals("ADMIN")) {
                Admin a = adminRepository.findByEmail(request.getEmail()).get();
                fullName = a.getFullName();
            } else if (role.equals("PHARMACIST")) {
                Pharmacist p = pharmacistRepository.findByEmail(request.getEmail()).get();
                fullName = p.getFullName();
            }

            AuthResponse resp = new AuthResponse(token, "Bearer", request.getEmail(), fullName, String.valueOf(java.util.List.of(role)));
            return ResponseEntity.ok(resp);

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        } catch (DisabledException ex) {
            return ResponseEntity.status(403).body(Map.of("error", "User disabled"));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(Map.of("error", "Authentication failed"));
        }
    }

    // ----------------- LOGOUT -----------------
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(name = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(Map.of("error", "No Bearer token provided"));
        }
        String token = authHeader.substring(7);
        try {
            long ttl = jwtUtil.extractExpiration(token).getTime() - System.currentTimeMillis();
            if (ttl <= 0) return ResponseEntity.ok(Map.of("message", "Token already expired"));
            blacklistService.blacklist(token, ttl);
            return ResponseEntity.ok(Map.of("message", "Logged out"));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid token"));
        }
    }

    // ----------------- CURRENT USER INFO -----------------
    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader(name = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("error", "No token"));
        }
        String token = authHeader.substring(7);
        if (blacklistService.isBlacklisted(token))
            return ResponseEntity.status(401).body(Map.of("error", "Token blacklisted"));
        try {
            String email = jwtUtil.extractUsername(token);
            String role = jwtUtil.extractRoles(token).get(0).replace("ROLE_", "");
            String fullName = "";

            if (role.equals("USER")) fullName = userRepository.findByEmail(email).get().getFullName();
            else if (role.equals("ADMIN")) fullName = adminRepository.findByEmail(email).get().getFullName();
            else if (role.equals("PHARMACIST")) fullName = pharmacistRepository.findByEmail(email).get().getFullName();

            return ResponseEntity.ok(Map.of(
                    "email", email,
                    "fullName", fullName,
                    "role", role
            ));
        } catch (Exception ex) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
        }
    }

    // ----------------- AVAILABLE ROLES -----------------
    @GetMapping("/roles")
    public ResponseEntity<?> roles() {
        return ResponseEntity.ok(java.util.List.of("USER", "ADMIN", "PHARMACIST"));
    }
}
