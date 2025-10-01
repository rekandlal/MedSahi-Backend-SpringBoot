package com.demo.medsahispringboot.Service;

import com.demo.medsahispringboot.Entity.Admin;
import com.demo.medsahispringboot.Entity.Pharmacist;
import com.demo.medsahispringboot.Entity.User;
import com.demo.medsahispringboot.Repository.AdminRepository;
import com.demo.medsahispringboot.Repository.PharmacistRepository;
import com.demo.medsahispringboot.Repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;
    private final AdminRepository adminRepo;
    private final PharmacistRepository pharmacistRepo;

    public JwtUserDetailsService(UserRepository userRepo,
                                 AdminRepository adminRepo,
                                 PharmacistRepository pharmacistRepo) {
        this.userRepo = userRepo;
        this.adminRepo = adminRepo;
        this.pharmacistRepo = pharmacistRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (userRepo.findByEmail(email).isPresent()) {
            User u = userRepo.findByEmail(email).get();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(u.getEmail())
                    .password(u.getPassword())
                    .authorities(Set.of(new SimpleGrantedAuthority("ROLE_USER")))
                    .disabled(!u.isEnabled())
                    .build();
        } else if (adminRepo.findByEmail(email).isPresent()) {
            Admin a = adminRepo.findByEmail(email).get();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(a.getEmail())
                    .password(a.getPassword())
                    .authorities(Set.of(new SimpleGrantedAuthority("ROLE_ADMIN")))
                    .disabled(!a.isEnabled())
                    .build();
        } else if (pharmacistRepo.findByEmail(email).isPresent()) {
            Pharmacist p = pharmacistRepo.findByEmail(email).get();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(p.getEmail())
                    .password(p.getPassword())
                    .authorities(Set.of(new SimpleGrantedAuthority("ROLE_PHARMACIST")))
                    .disabled(!p.isEnabled())
                    .build();
        }
        throw new UsernameNotFoundException("User not found");
    }
}
