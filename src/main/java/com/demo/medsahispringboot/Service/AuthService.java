package com.demo.medsahispringboot.Service;


import com.demo.medsahispringboot.Dto.RegisterRequest;
import com.demo.medsahispringboot.Entity.Role;
import com.demo.medsahispringboot.Entity.User;
import com.demo.medsahispringboot.Repository.RoleRepository;
import com.demo.medsahispringboot.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerNewUser(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        User u = new User();
        u.setEmail(req.getEmail());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setFullName(req.getFullName());
        u.setPhone(req.getPhone());
        u.setEnabled(true);

        // set role: default USER
        String wantedRole = (req.getRole() == null || req.getRole().isBlank()) ? "USER" : req.getRole().toUpperCase();
        Role role = roleRepository.findByName(wantedRole)
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName(wantedRole);
                    return roleRepository.save(r);
                });
        u.getRoles().add(role);

        return userRepository.save(u);
    }
}

