package com.demo.medsahispringboot.Service.Impl;

import com.demo.medsahispringboot.Dto.MedicineDto;
import com.demo.medsahispringboot.Entity.GenericMedicine;
import com.demo.medsahispringboot.Entity.Medicine;
import com.demo.medsahispringboot.Repository.MedicineRepository;
import com.demo.medsahispringboot.Repository.GenericMedicineRepository;
import com.demo.medsahispringboot.Service.MedicineService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;
    private final GenericMedicineRepository genericMedicineRepository;

    public MedicineServiceImpl(MedicineRepository medicineRepository, GenericMedicineRepository genericMedicineRepository) {
        this.medicineRepository = medicineRepository;
        this.genericMedicineRepository = genericMedicineRepository;
    }

    // Branded medicines search
    @Override
    public List<MedicineDto> searchMedicine(String keyword) {
        List<Medicine> meds = medicineRepository.findByBrandedNameContainingIgnoreCase(keyword);

        return meds.stream().map(m -> {
            List<MedicineDto.GenericDto> generics = m.getGenerics().stream().map(g ->
                    new MedicineDto.GenericDto(
                            g.getName(), g.getIngredient(), g.getDosage(),
                            g.getManufacturer(), g.getMrp(), g.getFinalPrice(), g.getForm()
                    )
            ).limit(5).collect(Collectors.toList());

            return new MedicineDto(
                    m.getBrandedName(), m.getIngredient(), m.getDosage(),
                    m.getManufacturer(), m.getMrp(), m.getFinalPrice(),
                    m.getForm(), generics
            );
        }).collect(Collectors.toList());
    }

    // Generic medicines search
    @Override
    public List<GenericMedicine> searchGeneric(String keyword) {
        return genericMedicineRepository.searchByName(keyword);
    }

    // Combined search: Branded + Generic
    @Override
    public CombinedSearchResult searchCombined(String keyword) {
        List<MedicineDto> brandedResults = searchMedicine(keyword);
        List<GenericMedicine> genericResults = searchGeneric(keyword);
        return new CombinedSearchResult(brandedResults, genericResults);
    }

    @Override
    public Medicine addMedicine(Medicine medicine) {
        return medicineRepository.save(medicine);
    }

    @Override
    public Optional<Medicine> findById(Long id) {
        return medicineRepository.findById(id);
    }

    @Override
    public boolean deleteById(Long id) {
        if (!medicineRepository.existsById(id)) return false;
        medicineRepository.deleteById(id);
        return true;
    }

    @Override public List<Medicine> findByAddedBy(String addedBy) { return medicineRepository.findByAddedBy(addedBy); }

    // DTO for combined search result
    public static class CombinedSearchResult {
        public List<MedicineDto> branded;
        public List<GenericMedicine> generic;

        public CombinedSearchResult(List<MedicineDto> branded, List<GenericMedicine> generic) {
            this.branded = branded;
            this.generic = generic;
        }
    }
}
