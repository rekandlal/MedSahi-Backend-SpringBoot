package com.demo.medsahispringboot.Controller;

import com.demo.medsahispringboot.Entity.GenericMedicine;
import com.demo.medsahispringboot.Entity.Medicine;
import com.demo.medsahispringboot.Repository.GenericMedicineRepository;
import com.demo.medsahispringboot.Service.MedicineService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/pharmacist")
@PreAuthorize("hasRole('PHARMACIST')")
public class PharmacistController {

    private final MedicineService medicineService;
    private final GenericMedicineRepository genericRepo;

    public PharmacistController(MedicineService medicineService, GenericMedicineRepository genericRepo) {
        this.medicineService = medicineService;
        this.genericRepo = genericRepo;
    }

    // ---------------- BRANDED MEDICINE ADD ----------------
    @PostMapping("/add/branded")
    public ResponseEntity<Object> addBrandedMedicine(@RequestBody Medicine medicine,
                                                     Authentication authentication) {
        String pharmacistEmail = authentication.getName();
        medicine.setAddedBy(pharmacistEmail);

        Medicine saved = medicineService.addMedicine(medicine);

        return ResponseEntity.ok(Map.of(
                "message", "Branded medicine added successfully",
                "brandedMedicine", saved
        ));
    }

    // ---------------- GENERIC MEDICINE ADD ----------------
    @PostMapping("/add/generic/{brandedId}")
    public ResponseEntity<Object> addGenericMedicine(@PathVariable Long brandedId,
                                                     @RequestBody GenericMedicine generic,
                                                     Authentication authentication) {
        String pharmacistEmail = authentication.getName();

        // Find branded medicine first
        Optional<Medicine> brandedOpt = medicineService.findById(brandedId);
        if (brandedOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Branded medicine not found"));
        }

        Medicine branded = brandedOpt.get();
        generic.setMedicine(branded); // link generic to branded
        GenericMedicine savedGeneric = genericRepo.save(generic);

        return ResponseEntity.ok(Map.of(
                "message", "Generic medicine added successfully",
                "brandedMedicine", branded.getBrandedName(),
                "genericMedicine", savedGeneric
        ));
    }

    // UPDATE MEDICINE (only if this pharmacist added it)
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateMedicine(@PathVariable Long id,
                                            @RequestBody Medicine medicine,
                                            Authentication authentication) {
        String pharmacistEmail = authentication.getName();
        return medicineService.findById(id)
                .map(existing -> {
                    if (!existing.getAddedBy().equals(pharmacistEmail)) {
                        return ResponseEntity.status(403).body(Map.of("error", "You cannot update others' medicines"));
                    }

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
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of("error", "Medicine not found")));
    }

    // DELETE MEDICINE (only if this pharmacist added it)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMedicine(@PathVariable Long id, Authentication authentication) {
        String pharmacistEmail = authentication.getName();

        return medicineService.findById(id)
                .map(existing -> {
                    if (!existing.getAddedBy().equals(pharmacistEmail)) {
                        return ResponseEntity.status(403)
                                .body(Map.of("error", "You cannot delete others' medicines"));
                    }
                    medicineService.deleteById(id);
                    return ResponseEntity.ok(Map.of("message", "Medicine deleted successfully"));
                })
                .orElseGet(() -> ResponseEntity.status(404)
                        .body(Map.of("error", "Medicine not found")));
    }

    // GET ALL MEDICINES added by logged-in Pharmacist
    @GetMapping("/my-medicines")
    public ResponseEntity<List<Medicine>> getMyMedicines(Authentication authentication) {
        String pharmacistEmail = authentication.getName();
        List<Medicine> myMeds = medicineService.findByAddedBy(pharmacistEmail);
        return ResponseEntity.ok(myMeds);
    }


    // Top 10 Sold Medicines by Area
    @GetMapping("/analytics/top-sold")
    public ResponseEntity<Map<String, Object>> getTopSoldMedicines() {

        List<Map<String, Object>> topSold = List.of(
                Map.of("medicine", "Atorvastatin", "sold", 120, "area", "Delhi"),
                Map.of("medicine", "Paracetamol", "sold", 100, "area", "Mumbai"),
                Map.of("medicine", "Amoxicillin", "sold", 85, "area", "Bangalore"),
                Map.of("medicine", "Ciprofloxacin", "sold", 70, "area", "Hyderabad"),
                Map.of("medicine", "Amlodipine", "sold", 65, "area", "Chennai"),
                Map.of("medicine", "Metformin", "sold", 60, "area", "Pune"),
                Map.of("medicine", "Ibuprofen", "sold", 55, "area", "Kolkata"),
                Map.of("medicine", "Azithromycin", "sold", 50, "area", "Jaipur"),
                Map.of("medicine", "Pantoprazole", "sold", 48, "area", "Lucknow"),
                Map.of("medicine", "Losartan", "sold", 45, "area", "Ahmedabad")
        );

        // Total sold count
        int totalSold = topSold.stream().mapToInt(m -> (int) m.get("sold")).sum();

        // Response body with extra info
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("totalSold", totalSold);
        response.put("topSold", topSold);
        response.put("reportGeneratedBy", "MedSahi Analytics");

        // Custom headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Analytics-Version", "1.0");
        headers.add("X-Report-Type", "Top Sold Medicines");

        return ResponseEntity.ok().headers(headers).body(response);
    }

    // Top 10 Wasted Medicines by Area
    @GetMapping("/analytics/top-waste")
    public ResponseEntity<Map<String, Object>> getTopWastedMedicines() {

        List<Map<String, Object>> topWaste = List.of(
                Map.of("medicine", "Aspirin", "wasted", 15, "area", "Delhi"),
                Map.of("medicine", "Metformin", "wasted", 12, "area", "Mumbai"),
                Map.of("medicine", "Ibuprofen", "wasted", 10, "area", "Bangalore"),
                Map.of("medicine", "Omeprazole", "wasted", 9, "area", "Hyderabad"),
                Map.of("medicine", "Clopidogrel", "wasted", 8, "area", "Chennai"),
                Map.of("medicine", "Cetirizine", "wasted", 7, "area", "Pune"),
                Map.of("medicine", "Levothyroxine", "wasted", 6, "area", "Kolkata"),
                Map.of("medicine", "Amoxicillin", "wasted", 6, "area", "Jaipur"),
                Map.of("medicine", "Paracetamol", "wasted", 5, "area", "Lucknow"),
                Map.of("medicine", "Diclofenac", "wasted", 5, "area", "Ahmedabad")
        );

        // Total wasted count
        int totalWasted = topWaste.stream().mapToInt(m -> (int) m.get("wasted")).sum();

        // Response body with extra info
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("totalWasted", totalWasted);
        response.put("topWaste", topWaste);
        response.put("reportGeneratedBy", "MedSahi Analytics");

        // Custom headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Analytics-Version", "1.0");
        headers.add("X-Report-Type", "Top Wasted Medicines");

        return ResponseEntity.ok().headers(headers).body(response);
    }
}