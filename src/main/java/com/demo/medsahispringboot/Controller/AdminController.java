package com.demo.medsahispringboot.Controller;

import com.demo.medsahispringboot.Entity.User;
import com.demo.medsahispringboot.Repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserRepository userRepo;

    public AdminController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    // ✅ All users
    @GetMapping("/users")
    public ResponseEntity<?> allUsers() {
        List<User> users = userRepo.findAll();
        return ResponseEntity.ok(users);
    }

    // ✅ Block/Enable User
    @PutMapping("/toggle/{id}")
    public ResponseEntity<?> toggleUser(@PathVariable Long id) {
        User u = userRepo.findById(id).orElseThrow();
        u.setEnabled(!u.isEnabled());
        userRepo.save(u);
        return ResponseEntity.ok(Map.of("message", "User status updated", "enabled", u.isEnabled()));
    }

    // ✅ Delete User
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userRepo.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }
}
