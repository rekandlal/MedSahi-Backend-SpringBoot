package com.demo.medsahispringboot.Service.Impl;

import com.demo.medsahispringboot.Entity.MedicineInfo;
import com.demo.medsahispringboot.Repository.MedicineInfoRepository;
import com.demo.medsahispringboot.Service.MedicineInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicineInfoServiceImpl implements MedicineInfoService {

    private final MedicineInfoRepository medicineInfoRepository;

    public MedicineInfoServiceImpl(MedicineInfoRepository medicineInfoRepository) {
        this.medicineInfoRepository = medicineInfoRepository;
    }

    @Override
    public MedicineInfo addMedicineInfo(MedicineInfo medicineInfo) {
        return medicineInfoRepository.save(medicineInfo);
    }

    @Override
    public List<MedicineInfo> getAllMedicineInfo() {
        return medicineInfoRepository.findAll();
    }

    @Override
    public MedicineInfo getMedicineInfoById(Long id) {
        return medicineInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MedicineInfo not found with id: " + id));
    }

    @Override
    public MedicineInfo updateMedicineInfo(Long id, MedicineInfo medicineInfo) {
        MedicineInfo existing = getMedicineInfoById(id);
        existing.setName(medicineInfo.getName());
        existing.setDescription(medicineInfo.getDescription());
        existing.setDosageForm(medicineInfo.getDosageForm());
        existing.setStrength(medicineInfo.getStrength());
        existing.setExpiryDate(medicineInfo.getExpiryDate());
        return medicineInfoRepository.save(existing);
    }

    @Override
    public void deleteMedicineInfoById(Long id) {
        medicineInfoRepository.deleteById(id);
    }
}
