package com.demo.medsahispringboot.Controller;


import com.demo.medsahispringboot.Configuration.JwtUtil;
import com.demo.medsahispringboot.Dto.AuthRequest;
import com.demo.medsahispringboot.Dto.AuthResponse;
import com.demo.medsahispringboot.Dto.RegisterRequest;
import com.demo.medsahispringboot.Entity.User;
import com.demo.medsahispringboot.Repository.UserRepository;
import com.demo.medsahispringboot.Service.AuthService;
import com.demo.medsahispringboot.Service.JwtUserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final JwtUserDetailsService userDetailsService;
    private final AuthService authService;
    private final UserRepository userRepository;

    // simple in-memory blacklist (demo). For production use Redis or DB.
    private final Map<String, Long> tokenBlacklist = new ConcurrentHashMap<>();

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          JwtUserDetailsService userDetailsService,
                          AuthService authService,
                          UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        try {
            User user = authService.registerNewUser(req);
            return ResponseEntity.ok(Map.of(
                    "message", "User registered",
                    "email", user.getEmail()
            ));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(Map.of("error", "Registration failed"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            // if authentication successful
            final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
            final String token = jwtUtil.generateToken(userDetails.getUsername());

            // optional: save token id in DB if you want server-side control

            // load extra user info
            var userOpt = userRepository.findByEmail(request.getEmail());
            String fullName = userOpt.map(User::getFullName).orElse("");

            AuthResponse resp = new AuthResponse(token, "Bearer", request.getEmail(), fullName);
            return ResponseEntity.ok(resp);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        } catch (DisabledException ex) {
            return ResponseEntity.status(403).body(Map.of("error", "User disabled"));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(Map.of("error", "Authentication failed"));
        }
    }

    /**
     * Logout endpoint (demo) - server-side invalidation: we store token in blacklist until it expires.
     * Client should delete token locally as well.
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(name = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(Map.of("error", "No Bearer token provided"));
        }
        String token = authorizationHeader.substring(7);
        try {
            // parse expiry to know how long to keep in blacklist (optional)
            var exp = jwtUtil.extractExpiration(token);
            long ttlMs = exp.getTime() - System.currentTimeMillis();
            if (ttlMs <= 0) {
                return ResponseEntity.ok(Map.of("message", "Token already expired"));
            }
            tokenBlacklist.put(token, System.currentTimeMillis() + ttlMs);
            return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid token"));
        }
    }

    // helper to check token blacklisted — you should integrate this check in JwtFilter
    public boolean isBlacklisted(String token) {
        Long expiry = tokenBlacklist.get(token);
        if (expiry == null) return false;
        if (System.currentTimeMillis() > expiry) {
            tokenBlacklist.remove(token);
            return false;
        }
        return true;
    }
}

