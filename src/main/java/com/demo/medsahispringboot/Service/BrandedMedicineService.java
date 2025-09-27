package com.demo.medsahispringboot.Service;

import com.demo.medsahispringboot.Entity.BrandedMedicine;

import java.util.List;

public interface BrandedMedicineService {

    BrandedMedicine saveBrandedMedicine(BrandedMedicine brandedMedicine,Long genericMedicineId);
    List<BrandedMedicine> getAllBrandedMedicines();
    BrandedMedicine getBrandedMedicineById(Long id);
    BrandedMedicine updateBrandedMedicine(Long id,BrandedMedicine brandedMedicine);
    void deleteBrandedMedicine(Long id);
    List<BrandedMedicine> getBrandedByGenericId(Long genericMedicineId);

}
