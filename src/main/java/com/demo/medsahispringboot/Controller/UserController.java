package com.demo.medsahispringboot.Controller;

import com.demo.medsahispringboot.Entity.User;
import com.demo.medsahispringboot.Repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasRole('USER')")

public class UserController {

    private final UserRepository userRepo;

    public UserController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    // ✅ Get my profile
    @GetMapping("/profile")
    public ResponseEntity<?> profile(Authentication auth) {
        User u = userRepo.findByEmail(auth.getName()).orElseThrow();
        return ResponseEntity.ok(Map.of(
                "email", u.getEmail(),
                "fullName", u.getFullName(),
                "phone", u.getPhone(),
                "rewardCoins", u.getRewardCoins(),
                "roles", u.getRoles().stream().map(r -> r.getName()).toList()
        ));
    }

    // ✅ Reward Points (Example API)
    @PostMapping("/add-coins")
    public ResponseEntity<?> addCoins(Authentication auth, @RequestParam Long coins) {
        User u = userRepo.findByEmail(auth.getName()).orElseThrow();
        u.setRewardCoins(u.getRewardCoins() + coins);
        userRepo.save(u);
        return ResponseEntity.ok(Map.of("message", "Coins added", "total", u.getRewardCoins()));
    }

    // ✅ Example: Lab Test
    @GetMapping("/labtest")
    public ResponseEntity<?> labTest() {
        return ResponseEntity.ok(Map.of("availableTests", new String[]{"Blood Test", "X-Ray", "MRI"}));
    }

    // ✅ Example: Doctor Consultation
    @GetMapping("/consult")
    public ResponseEntity<?> consult() {
        return ResponseEntity.ok(Map.of("availableDoctors", new String[]{"Dr. Sharma", "Dr. Patel"}));
    }

    // ✅ Subscription details
    @GetMapping("/subscription")
    public ResponseEntity<?> subscription() {
        return ResponseEntity.ok(Map.of(
                "plans", new String[]{"Basic", "Premium", "Pro"},
                "message", "Subscribe to access advanced features"
        ));
    }
}
