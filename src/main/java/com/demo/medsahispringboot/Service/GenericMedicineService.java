package com.demo.medsahispringboot.Service;

import com.demo.medsahispringboot.Entity.GenericMedicine;

import java.util.List;

public interface GenericMedicineService {
    //Used For saving the generic Medicine
    GenericMedicine saveGenericMedicine(GenericMedicine genericMedicine);

    List<GenericMedicine> getAllGenericMedicine();
    GenericMedicine getGenericMedicineById(Long id);
    GenericMedicine updateGenericMedicine(Long id,GenericMedicine genericMedicine);
    void deleteGenericMedicineById(Long id);
}
