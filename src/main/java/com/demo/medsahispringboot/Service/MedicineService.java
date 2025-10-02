package com.demo.medsahispringboot.Service;

import com.demo.medsahispringboot.Dto.MedicineDto;
import com.demo.medsahispringboot.Entity.GenericMedicine;
import com.demo.medsahispringboot.Entity.Medicine;
import com.demo.medsahispringboot.Service.Impl.MedicineServiceImpl;

import java.util.List;
import java.util.Optional;

public interface MedicineService {
    List<MedicineDto> searchMedicine(String keyword);
    List<GenericMedicine> searchGeneric(String keyword);

    // Combined search: Branded + Generic
    MedicineServiceImpl.CombinedSearchResult searchCombined(String keyword);

    Medicine addMedicine(Medicine medicine);
    Optional<Medicine> findById(Long id);
    boolean deleteById(Long id);

    List<Medicine> findByAddedBy(String addedBy);
}
