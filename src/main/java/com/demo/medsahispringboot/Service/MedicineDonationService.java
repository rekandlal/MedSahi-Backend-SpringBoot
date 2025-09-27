package com.demo.medsahispringboot.Service;

import com.demo.medsahispringboot.Entity.MedicineDonation;

import java.util.List;

public interface MedicineDonationService {
    MedicineDonation donateMedicine(MedicineDonation medicineDonation,Long userId,Long medicineInfoId);
    List<MedicineDonation> getDonationsByUser(Long userId);
    List<MedicineDonation> getDonationsByStatus(String status);
    MedicineDonation verifyDonation(Long donationId,String status);
}
