package com.demo.medsahispringboot.Service.Impl;

import com.demo.medsahispringboot.Entity.MedicineDonation;
import com.demo.medsahispringboot.Entity.MedicineInfo;
import com.demo.medsahispringboot.Entity.User;
import com.demo.medsahispringboot.Repository.MedicineDonationRepository;
import com.demo.medsahispringboot.Repository.MedicineInfoRepository;
import com.demo.medsahispringboot.Repository.UserRepository;
import com.demo.medsahispringboot.Service.MedicineDonationService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MedicineDonationServiceImpl implements MedicineDonationService {

    private final MedicineDonationRepository donationRepository;
    private final UserRepository userRepository;
    private final MedicineInfoRepository medicineInfoRepository;

    public MedicineDonationServiceImpl(MedicineDonationRepository donationRepository,
                                       UserRepository userRepository,
                                       MedicineInfoRepository medicineInfoRepository) {
        this.donationRepository = donationRepository;
        this.userRepository = userRepository;
        this.medicineInfoRepository = medicineInfoRepository;
    }

    @Override
    public MedicineDonation donateMedicine(MedicineDonation donation, Long userId, Long medicineInfoId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        MedicineInfo medicineInfo = medicineInfoRepository.findById(medicineInfoId)
                .orElseThrow(() -> new RuntimeException("MedicineInfo not found with id: " + medicineInfoId));

        donation.setUser(user);
        donation.setMedicineInfo(medicineInfo);
        donation.setDonationDate(LocalDate.now());
        donation.setStatus("PENDING");
        return donationRepository.save(donation);
    }

    @Override
    public List<MedicineDonation> getDonationsByUser(Long userId) {
        return donationRepository.findByUserId(userId);
    }

    @Override
    public List<MedicineDonation> getDonationsByStatus(String status) {
        return donationRepository.findByStatus(status);
    }

    @Override
    public MedicineDonation verifyDonation(Long donationId, String status) {
        MedicineDonation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new RuntimeException("Donation not found with id: " + donationId));
        donation.setStatus(status);
        return donationRepository.save(donation);
    }
}
