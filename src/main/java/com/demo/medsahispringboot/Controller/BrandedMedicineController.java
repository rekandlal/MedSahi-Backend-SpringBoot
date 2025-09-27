package com.demo.medsahispringboot.Controller;

import com.demo.medsahispringboot.Entity.BrandedMedicine;
import com.demo.medsahispringboot.Service.BrandedMedicineService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/branded-medicines")
public class BrandedMedicineController {

    private final BrandedMedicineService brandedMedicineService;

    public BrandedMedicineController(BrandedMedicineService brandedMedicineService) {
        this.brandedMedicineService = brandedMedicineService;
    }

    @PostMapping("/{genericMedicineId}")
    public BrandedMedicine create(@PathVariable Long genericMedicineId,
                                  @RequestBody BrandedMedicine brandedMedicine) {
        return brandedMedicineService.saveBrandedMedicine(brandedMedicine, genericMedicineId);
    }

    @GetMapping
    public List<BrandedMedicine> getAll() {
        return brandedMedicineService.getAllBrandedMedicines();
    }

    @GetMapping("/{id}")
    public BrandedMedicine getById(@PathVariable Long id) {
        return brandedMedicineService.getBrandedMedicineById(id);
    }

    @PutMapping("/{id}")
    public BrandedMedicine update(@PathVariable Long id, @RequestBody BrandedMedicine brandedMedicine) {
        return brandedMedicineService.updateBrandedMedicine(id, brandedMedicine);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        brandedMedicineService.deleteBrandedMedicine(id);
        return "BrandedMedicine deleted successfully with id: " + id;
    }

    @GetMapping("/generic/{genericId}")
    public List<BrandedMedicine> getByGeneric(@PathVariable Long genericId) {
        return brandedMedicineService.getBrandedByGenericId(genericId);
    }
}
