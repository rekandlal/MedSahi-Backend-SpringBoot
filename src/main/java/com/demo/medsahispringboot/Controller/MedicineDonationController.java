package com.demo.medsahispringboot.Controller;

import com.demo.medsahispringboot.Entity.MedicineDonation;
import com.demo.medsahispringboot.Service.MedicineDonationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicine-donations")
public class MedicineDonationController {

    private final MedicineDonationService donationService;
    public MedicineDonationController(MedicineDonationService donationService) {
        this.donationService = donationService;
    }

    @PostMapping("/{userId}/{medicineInfoId}")
    public MedicineDonation donate(@PathVariable Long userId,
                                   @PathVariable Long medicineInfoId,
                                   @RequestBody MedicineDonation donation) {
        return donationService.donateMedicine(donation, userId, medicineInfoId);
    }

    @GetMapping("/user/{userId}")
    public List<MedicineDonation> getByUser(@PathVariable Long userId) {
        return donationService.getDonationsByUser(userId);
    }

    @GetMapping("/status/{status}")
    public List<MedicineDonation> getByStatus(@PathVariable String status) {
        return donationService.getDonationsByStatus(status);
    }

    @PutMapping("/verify/{donationId}")
    public MedicineDonation verify(@PathVariable Long donationId, @RequestParam String status) {
        return donationService.verifyDonation(donationId, status);
    }
}
