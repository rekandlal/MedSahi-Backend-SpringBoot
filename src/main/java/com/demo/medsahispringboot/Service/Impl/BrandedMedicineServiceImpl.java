package com.demo.medsahispringboot.Service.Impl;

import com.demo.medsahispringboot.Entity.BrandedMedicine;
import com.demo.medsahispringboot.Entity.GenericMedicine;
import com.demo.medsahispringboot.Repository.BrandedMedicineRepository;
import com.demo.medsahispringboot.Repository.GenericMedicineRepository;
import com.demo.medsahispringboot.Service.BrandedMedicineService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandedMedicineServiceImpl implements BrandedMedicineService {

    private final BrandedMedicineRepository brandedMedicineRepository;
    private final GenericMedicineRepository genericMedicineRepository;

    public BrandedMedicineServiceImpl(BrandedMedicineRepository brandedMedicineRepository,
                                      GenericMedicineRepository genericMedicineRepository) {
        this.brandedMedicineRepository = brandedMedicineRepository;
        this.genericMedicineRepository = genericMedicineRepository;
    }

    @Override
    public BrandedMedicine saveBrandedMedicine(BrandedMedicine brandedMedicine, Long genericMedicineId) {
        GenericMedicine genericMedicine = genericMedicineRepository.findById(genericMedicineId)
                .orElseThrow(() -> new RuntimeException("GenericMedicine not found with id: " + genericMedicineId));
        brandedMedicine.setGenericMedicine(genericMedicine);
        return brandedMedicineRepository.save(brandedMedicine);
    }

    @Override
    public List<BrandedMedicine> getAllBrandedMedicines() {
        return brandedMedicineRepository.findAll();
    }

    @Override
    public BrandedMedicine getBrandedMedicineById(Long id) {
        return brandedMedicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BrandedMedicine not found with id: " + id));
    }

    @Override
    public BrandedMedicine updateBrandedMedicine(Long id, BrandedMedicine brandedMedicine) {
        BrandedMedicine existing = getBrandedMedicineById(id);
        existing.setBrandName(brandedMedicine.getBrandName());
        existing.setManufacturer(brandedMedicine.getManufacturer());
        existing.setPrice(brandedMedicine.getPrice());
        existing.setStock(brandedMedicine.getStock());
        return brandedMedicineRepository.save(existing);
    }

    @Override
    public void deleteBrandedMedicine(Long id) {
        brandedMedicineRepository.deleteById(id);
    }

    @Override
    public List<BrandedMedicine> getBrandedByGenericId(Long genericMedicineId) {
        return brandedMedicineRepository.findByGenericMedicineId(genericMedicineId);
    }
}
