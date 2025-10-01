package com.demo.medsahispringboot.Service;

import com.demo.medsahispringboot.Dto.RegisterRequest;
import com.demo.medsahispringboot.Entity.Admin;
import com.demo.medsahispringboot.Entity.Pharmacist;
import com.demo.medsahispringboot.Entity.User;
import com.demo.medsahispringboot.Repository.AdminRepository;
import com.demo.medsahispringboot.Repository.PharmacistRepository;
import com.demo.medsahispringboot.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final AdminRepository adminRepo;
    private final PharmacistRepository pharmacistRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepo,
                       AdminRepository adminRepo,
                       PharmacistRepository pharmacistRepo,
                       PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.adminRepo = adminRepo;
        this.pharmacistRepo = pharmacistRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public Object registerNewUser(RegisterRequest req) {
        String role = req.getRole().toUpperCase();

        switch (role) {
            case "USER":
                if (userRepo.existsByEmail(req.getEmail())) throw new RuntimeException("Email exists");
                User user = new User();
                user.setEmail(req.getEmail());
                user.setPassword(passwordEncoder.encode(req.getPassword()));
                user.setFullName(req.getFullName());
                user.setPhone(req.getPhone());
                return userRepo.save(user);

            case "ADMIN":
                if (adminRepo.existsByEmail(req.getEmail())) throw new RuntimeException("Email exists");
                Admin admin = new Admin();
                admin.setEmail(req.getEmail());
                admin.setPassword(passwordEncoder.encode(req.getPassword()));
                admin.setFullName(req.getFullName());
                admin.setPhone(req.getPhone());
                return adminRepo.save(admin);

            case "PHARMACIST":
                if (pharmacistRepo.existsByEmail(req.getEmail())) throw new RuntimeException("Email exists");
                Pharmacist ph = new Pharmacist();
                ph.setEmail(req.getEmail());
                ph.setPassword(passwordEncoder.encode(req.getPassword()));
                ph.setFullName(req.getFullName());
                ph.setPhone(req.getPhone());
                ph.setLicenseNumber("LIC-" + System.currentTimeMillis());
                return pharmacistRepo.save(ph);

            default:
                throw new RuntimeException("Invalid role");
        }
    }
}
