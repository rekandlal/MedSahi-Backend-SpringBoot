package com.demo.medsahispringboot.Repository;

import com.demo.medsahispringboot.Entity.GenericMedicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenericMedicineRepository extends JpaRepository<GenericMedicine, Long> {
    @Query("SELECT g FROM GenericMedicine g WHERE LOWER(g.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<GenericMedicine> searchByName(@Param("keyword") String keyword);
}

