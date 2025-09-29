package com.demo.medsahispringboot.Service;


import com.demo.medsahispringboot.Dto.MedicineDto;
import com.demo.medsahispringboot.Entity.Medicine;

import java.util.List;
import java.util.Optional;

public interface MedicineService {
    List<MedicineDto> searchMedicine(String keyword);
    Medicine addMedicine(Medicine medicine);

    // PharmacistController support
    Optional<Medicine> findById(Long id);
    boolean deleteById(Long id);
}

