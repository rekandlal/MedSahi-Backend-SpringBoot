package com.demo.medsahispringboot.Service.Impl;

import com.demo.medsahispringboot.Entity.GenericMedicine;
import com.demo.medsahispringboot.Repository.GenericMedicineRepository;
import com.demo.medsahispringboot.Service.GenericMedicineService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenericMedicineServiceImpl implements GenericMedicineService {

    private final GenericMedicineRepository genericMedicineRepository;

    public GenericMedicineServiceImpl(GenericMedicineRepository genericMedicineRepository) {
        this.genericMedicineRepository = genericMedicineRepository;
    }

    @Override
    public GenericMedicine saveGenericMedicine(GenericMedicine genericMedicine) {
        return genericMedicineRepository.save(genericMedicine);
    }

    @Override
    public List<GenericMedicine> getAllGenericMedicines() {
        return genericMedicineRepository.findAll();
    }

    @Override
    public GenericMedicine getGenericMedicineById(Long id) {
        return genericMedicineRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Generric Medicine Not Found with the Given Id:"+id));
    }

    @Override
    public GenericMedicine updateGenericMedicine(Long id,GenericMedicine genericMedicine) {
        GenericMedicine existing=getGenericMedicineById(id);
                existing.setGenericName(genericMedicine.getGenericName());
        existing.setTherapeuticUse(genericMedicine.getTherapeuticUse());
        existing.setSideEffects(genericMedicine.getSideEffects());
        return genericMedicineRepository.save(existing);
    }

    @Override
    public void deleteGenericMedicine(Long id){
        genericMedicineRepository.deleteById(id);
    }

}
