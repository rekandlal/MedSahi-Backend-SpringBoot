package com.demo.medsahispringboot.Controller;

import com.demo.medsahispringboot.Dto.MedicineDto;
import com.demo.medsahispringboot.Entity.Medicine;
import com.demo.medsahispringboot.Service.MedicineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicine")
public class MedicineController {

    private final MedicineService medicineService;

    public MedicineController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    // Public search endpoint
    @GetMapping("/public/search")
    public ResponseEntity<List<MedicineDto>> searchMedicine(@RequestParam String keyword) {
        List<MedicineDto> results = medicineService.searchMedicine(keyword);
        return ResponseEntity.ok(results);
    }

    // Admin/Pharmacist: add new medicine
    @PostMapping("/add")
    public ResponseEntity<?> addMedicine(@RequestBody Medicine medicine) {
        Medicine saved = medicineService.addMedicine(medicine);
        return ResponseEntity.ok(saved);
    }
}

