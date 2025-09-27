package com.demo.medsahispringboot.Service;

import com.demo.medsahispringboot.Entity.MedicineInfo;

import java.util.List;

public interface MedicineInfoService {
    MedicineInfo addMedicineInfo(MedicineInfo medicineInfo);
    List<MedicineInfo> getAllMedicineInfo();
    MedicineInfo getMedicineInfoById(Long id);
    MedicineInfo updateMedicineInfo(Long id,MedicineInfo medicineInfo);
    void deleteMedicineInfoById(Long id);
}
