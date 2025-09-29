package com.demo.medsahispringboot.Controller;

import com.demo.medsahispringboot.Entity.Medicine;
import com.demo.medsahispringboot.Service.MedicineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/pharmacist")
public class PharmacistController {

    private final MedicineService medicineService;

    public PharmacistController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    // ---------------------------
    // ADD NEW MEDICINE
    // ---------------------------
    @PostMapping("/add")
    public ResponseEntity<Object> addMedicine(@RequestBody Medicine medicine) {
        Medicine saved = medicineService.addMedicine(medicine);
        return ResponseEntity.ok(Map.of(
                "message", "Medicine added successfully",
                "medicine", saved
        ));
    }

    // ---------------------------
    // UPDATE MEDICINE
    // ---------------------------
    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateMedicine(@PathVariable Long id, @RequestBody Medicine medicine) {
        return medicineService.findById(id)
                .map(existing -> {
                    existing.setBrandedName(medicine.getBrandedName());
                    existing.setIngredient(medicine.getIngredient());
                    existing.setDosage(medicine.getDosage());
                    existing.setManufacturer(medicine.getManufacturer());
                    existing.setMrp(medicine.getMrp());
                    existing.setFinalPrice(medicine.getFinalPrice());
                    existing.setForm(medicine.getForm());
                    existing.setGenerics(medicine.getGenerics());

                    Medicine updated = medicineService.addMedicine(existing);

                    return ResponseEntity.ok(Map.of(
                            "message", "Medicine updated successfully",
                            "medicine", updated
                    ));
                })
                .orElseGet(() -> ResponseEntity.status(404).body(
                        Map.of("error", "Medicine not found")
                ));
    }

    // ---------------------------
    // DELETE MEDICINE
    // ---------------------------
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteMedicine(@PathVariable Long id) {
        boolean deleted = medicineService.deleteById(id);
        if (!deleted) {
            return ResponseEntity.status(404).body(
                    Map.of("error", "Medicine not found")
            );
        }
        return ResponseEntity.ok(Map.of("message", "Medicine deleted successfully"));
    }

    // ---------------------------
    // ANALYTICS: Top 5 Sold Medicines
    // ---------------------------
    @GetMapping("/analytics/top-sold")
    public ResponseEntity<List<Map<String, Object>>> getTopSoldMedicines() {
        // Ye abhi static hai, baad me DB query se nikalega
        List<Map<String, Object>> topSold = List.of(
                Map.of("medicine", "Atorvastatin", "sold", 120),
                Map.of("medicine", "Paracetamol", "sold", 100),
                Map.of("medicine", "Amoxicillin", "sold", 85)
        );
        return ResponseEntity.ok(topSold);
    }

    // ---------------------------
    // ANALYTICS: Top 5 Wasted Medicines
    // ---------------------------
    @GetMapping("/analytics/top-waste")
    public ResponseEntity<List<Map<String, Object>>> getTopWastedMedicines() {
        // Ye bhi static abhi ke liye
        List<Map<String, Object>> topWaste = List.of(
                Map.of("medicine", "Aspirin", "wasted", 15),
                Map.of("medicine", "Metformin", "wasted", 10),
                Map.of("medicine", "Ibuprofen", "wasted", 7)
        );
        return ResponseEntity.ok(topWaste);
    }
}
