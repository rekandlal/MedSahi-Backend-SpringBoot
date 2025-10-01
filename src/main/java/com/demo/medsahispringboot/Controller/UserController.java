package com.demo.medsahispringboot.Controller;

import com.demo.medsahispringboot.Entity.User;
import com.demo.medsahispringboot.Repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasRole('USER')")
public class UserController {

    private final UserRepository userRepo;

    public UserController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getProfile(Authentication auth) {
        String email = auth.getName();
        Optional<User> uOpt = userRepo.findByEmail(email);
        if (uOpt.isEmpty()) return ResponseEntity.status(404).body("User not found");
        User u = uOpt.get();
        return ResponseEntity.ok(Map.of(
                "email", u.getEmail(),
                "fullName", u.getFullName(),
                "phone", u.getPhone(),
                "rewardCoins", u.getRewardCoins()
        ));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(Authentication auth, @RequestBody Map<String,String> req) {
        String email = auth.getName();
        Optional<User> uOpt = userRepo.findByEmail(email);
        if (uOpt.isEmpty()) return ResponseEntity.status(404).body("User not found");
        User u = uOpt.get();
        if (req.containsKey("fullName")) u.setFullName(req.get("fullName"));
        if (req.containsKey("phone")) u.setPhone(req.get("phone"));
        userRepo.save(u);
        return ResponseEntity.ok("Profile updated");
    }

    @PostMapping("/reward/add")
    public ResponseEntity<?> addRewardCoins(Authentication auth, @RequestParam Long coins) {
        String email = auth.getName();
        Optional<User> uOpt = userRepo.findByEmail(email);
        if (uOpt.isEmpty()) return ResponseEntity.status(404).body("User not found");
        User u = uOpt.get();
        u.setRewardCoins(u.getRewardCoins() + coins);
        userRepo.save(u);
        return ResponseEntity.ok(Map.of("message","Reward added","totalCoins", u.getRewardCoins()));
    }
}
