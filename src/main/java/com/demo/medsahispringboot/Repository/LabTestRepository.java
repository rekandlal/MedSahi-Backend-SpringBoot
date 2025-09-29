package com.demo.medsahispringboot.Repository;


import com.demo.medsahispringboot.Entity.LabTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface LabTestRepository extends JpaRepository<LabTest, Long> {
    List<LabTest> findByUserId(Long userId);
}
