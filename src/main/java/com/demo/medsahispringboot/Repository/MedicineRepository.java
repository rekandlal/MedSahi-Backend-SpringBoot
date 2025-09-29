package com.demo.medsahispringboot.Repository;


import com.demo.medsahispringboot.Entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    List<Medicine> findByBrandedNameContainingIgnoreCase(String keyword);
}

