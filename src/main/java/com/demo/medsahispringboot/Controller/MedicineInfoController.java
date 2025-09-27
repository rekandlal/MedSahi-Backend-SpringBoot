package com.demo.medsahispringboot.Controller;

import com.demo.medsahispringboot.Entity.MedicineInfo;
import com.demo.medsahispringboot.Service.MedicineInfoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicine-info")
public class MedicineInfoController {

    private final MedicineInfoService medicineInfoService;
    public MedicineInfoController(MedicineInfoService medicineInfoService) {
        this.medicineInfoService = medicineInfoService;
    }

    @PostMapping
    public MedicineInfo create(@RequestBody MedicineInfo medicineInfo) {
        return medicineInfoService.addMedicineInfo(medicineInfo);
    }

    @GetMapping
    public List<MedicineInfo> getAll() {
        return medicineInfoService.getAllMedicineInfo();
    }

    @GetMapping("/{id}")
    public MedicineInfo getById(@PathVariable Long id) {
        return medicineInfoService.getMedicineInfoById(id);
    }

    @PutMapping("/{id}")
    public MedicineInfo update(@PathVariable Long id, @RequestBody MedicineInfo medicineInfo) {
        return medicineInfoService.updateMedicineInfo(id, medicineInfo);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        medicineInfoService.deleteMedicineInfoById(id);
        return "MedicineInfo deleted successfully with id: " + id;
    }
}
