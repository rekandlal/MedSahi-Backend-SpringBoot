package com.demo.medsahispringboot.Repository;

import com.demo.medsahispringboot.Entity.Medicine;
import com.demo.medsahispringboot.Entity.GenericMedicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    List<Medicine> findByBrandedNameContainingIgnoreCase(String keyword);

    @Query("SELECT g FROM GenericMedicine g WHERE LOWER(g.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<GenericMedicine> searchGenericByName(@Param("keyword") String keyword);
}
