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

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // GET CURRENT LOGGED-IN USER PROFILE
    @GetMapping("/me")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        String email = authentication.getName();
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return ResponseEntity.status(404).body("User not found");
        User user = userOpt.get();
        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "fullName", user.getFullName(),
                "email", user.getEmail(),
                "phone", user.getPhone(),
                "rewardCoins", user.getRewardCoins()
        ));
    }

    // UPDATE USER PROFILE
    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(Authentication authentication,
                                           @RequestBody Map<String, String> req) {
        String email = authentication.getName();
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return ResponseEntity.status(404).body("User not found");
        User user = userOpt.get();
        if (req.containsKey("fullName")) user.setFullName(req.get("fullName"));
        if (req.containsKey("phone")) user.setPhone(req.get("phone"));
        userRepository.save(user);
        return ResponseEntity.ok("Profile updated successfully");
    }

    // ADD REWARD COINS
    @PostMapping("/reward/add")
    public ResponseEntity<?> addRewardCoins(Authentication authentication,
                                            @RequestParam Long coins) {
        String email = authentication.getName();
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return ResponseEntity.status(404).body("User not found");
        User user = userOpt.get();
        user.setRewardCoins(user.getRewardCoins() + coins);
        userRepository.save(user);
        return ResponseEntity.ok(Map.of(
                "message", "Reward added",
                "totalCoins", user.getRewardCoins()
        ));
    }
}
