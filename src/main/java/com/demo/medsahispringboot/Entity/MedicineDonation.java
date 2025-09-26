package com.demo.medsahispringboot.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineDonation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate donationDate;
    private String status; // PENDING, VERIFIED, REJECTED, ACCEPTED

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // 👈 yeh naam "mappedBy" ke liye match hona chahiye

    @ManyToOne
    @JoinColumn(name = "medicine_info_id")
    private MedicineInfo medicineInfo;
}
