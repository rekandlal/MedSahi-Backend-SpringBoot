package com.demo.medsahispringboot.Controller;

import com.demo.medsahispringboot.Entity.GenericMedicine;
import com.demo.medsahispringboot.Service.GenericMedicineService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/generic-medicines ")
public class GenericMedicineController {

    private final GenericMedicineService genericMedicineService;

    public GenericMedicineController(GenericMedicineService genericMedicineService) {
        this.genericMedicineService = genericMedicineService;
    }

    @PostMapping
    public GenericMedicine create(@RequestBody GenericMedicine genericMedicine) {
        return genericMedicineService.saveGenericMedicine(genericMedicine);
    }

    @GetMapping
    public List<GenericMedicine> getAll() {
        return genericMedicineService.getAllGenericMedicines();
    }

    @GetMapping("/{id}")
    public GenericMedicine getById(@PathVariable Long id) {
        return genericMedicineService.getGenericMedicineById(id);
    }

    @PutMapping("/{id}")
    public GenericMedicine update(@PathVariable Long id, @RequestBody GenericMedicine genericMedicine) {
        return genericMedicineService.updateGenericMedicine(id, genericMedicine);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        genericMedicineService.deleteGenericMedicine(id);
        return "GenericMedicine deleted successfully with id: " + id;
    }

}
