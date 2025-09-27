package com.demo.medsahispringboot.Repository;

import com.demo.medsahispringboot.Entity.MedicineDonation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicineDonationRepository extends JpaRepository<MedicineDonation,Long> {
    List<MedicineDonation> findByUserId(Long userId);
    List<MedicineDonation> findByStatus(String status);
}
