package com.demo.medsahispringboot.Service.Impl;


import com.demo.medsahispringboot.Entity.Role;
import com.demo.medsahispringboot.Entity.User;
import com.demo.medsahispringboot.Repository.RoleRepository;
import com.demo.medsahispringboot.Repository.UserRepository;
import com.demo.medsahispringboot.Service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(User user) {
        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Default role USER
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Optional<Role> userRoleOpt = roleRepository.findByName("USER");
            Role userRole = userRoleOpt.orElseGet(() -> {
                Role newRole = new Role();
                newRole.setName("USER");
                return roleRepository.save(newRole);
            });
            user.setRoles(Set.of(userRole));
        }

        // Default reward coins
        if (user.getRewardCoins() == null) user.setRewardCoins(0L);

        user.setEnabled(true);
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean addRewardCoins(String email, Long coins) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return false;
        User user = userOpt.get();
        user.setRewardCoins(user.getRewardCoins() + coins);
        userRepository.save(user);
        return true;
    }
}
