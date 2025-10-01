package com.demo.medsahispringboot.Repository;

import com.demo.medsahispringboot.Entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
