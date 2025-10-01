package com.demo.medsahispringboot.Dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationResponseDTO {
    private Long id;
    private String issueDescription;
    private String status;
    private DoctorInfo doctor;
    private LocalDateTime requestedAt;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DoctorInfo {
        private Long id;
        private String name;
        private String specialization;
        private Double consultationFee;
    }
}
