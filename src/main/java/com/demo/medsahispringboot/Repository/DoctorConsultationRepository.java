package com.demo.medsahispringboot.Repository;

import com.demo.medsahispringboot.Entity.DoctorConsultation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorConsultationRepository extends JpaRepository<DoctorConsultation, Long> {
    List<DoctorConsultation> findByUserId(Long userId);
}
