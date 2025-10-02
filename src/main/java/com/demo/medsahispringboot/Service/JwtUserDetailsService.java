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
import java.util.stream.Collectors;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final PharmacistRepository pharmacistRepository;

    public JwtUserDetailsService(UserRepository userRepository, AdminRepository adminRepository, PharmacistRepository pharmacistRepository) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.pharmacistRepository = pharmacistRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // User
        if(userRepository.findByEmail(email).isPresent()){
            User u = userRepository.findByEmail(email).get();
            return new org.springframework.security.core.userdetails.User(
                    u.getEmail(),
                    u.getPassword(),
                    u.getRoles().stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName())).toList()
            );
        }
        // Admin
        else if(adminRepository.findByEmail(email).isPresent()){
            Admin a = adminRepository.findByEmail(email).get();
            return new org.springframework.security.core.userdetails.User(
                    a.getEmail(),
                    a.getPassword(),
                    a.getRoles().stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName())).toList()
            );
        }
        // Pharmacist
        else if(pharmacistRepository.findByEmail(email).isPresent()){
            Pharmacist p = pharmacistRepository.findByEmail(email).get();
            return new org.springframework.security.core.userdetails.User(
                    p.getEmail(),
                    p.getPassword(),
                    p.getRoles().stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName())).toList()
            );
        }
        else throw new UsernameNotFoundException("User not found with email: " + email);
    }

}
