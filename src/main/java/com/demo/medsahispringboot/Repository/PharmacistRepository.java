package com.demo.medsahispringboot.Repository;

import com.demo.medsahispringboot.Entity.Pharmacist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PharmacistRepository extends JpaRepository<Pharmacist, Long> {
    Optional<Pharmacist> findByEmail(String email);
    boolean existsByEmail(String email);
}
