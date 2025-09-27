package com.demo.medsahispringboot.Repository;

import com.demo.medsahispringboot.Entity.BrandedMedicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandedMedicineRepository extends JpaRepository<BrandedMedicine , Long> {

    // custom query: find all branded medicines by generic medicine id
    List<BrandedMedicine> findByGenericMedicineId(Long genericMedicineId);
}
