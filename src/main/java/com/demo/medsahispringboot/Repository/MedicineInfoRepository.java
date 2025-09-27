package com.demo.medsahispringboot.Repository;

import com.demo.medsahispringboot.Entity.MedicineInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineInfoRepository extends JpaRepository<MedicineInfo,Long> {
}
