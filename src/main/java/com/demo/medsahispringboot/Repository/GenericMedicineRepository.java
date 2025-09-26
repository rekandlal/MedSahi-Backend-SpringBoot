package com.demo.medsahispringboot.Repository;

import com.demo.medsahispringboot.Entity.GenericMedicine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenericMedicineRepository extends JpaRepository<GenericMedicine,Long> {

}
