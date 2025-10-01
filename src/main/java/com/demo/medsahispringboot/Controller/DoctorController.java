package com.demo.medsahispringboot.Controller;

import com.demo.medsahispringboot.Dto.ConsultationResponseDTO;
import com.demo.medsahispringboot.Entity.*;
import com.demo.medsahispringboot.Repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    private final DoctorConsultationRepository consultationRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;

    public DoctorController(DoctorConsultationRepository consultationRepository,
                            UserRepository userRepository,
                            DoctorRepository doctorRepository) {
        this.consultationRepository = consultationRepository;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
    }

    // ✅ Request Consultation
    @PostMapping("/request")
    public ResponseEntity<?> requestConsultation(Authentication auth,
                                                 @RequestParam String issueDescription,
                                                 @RequestParam Long doctorId) {
        Optional<User> userOpt = userRepository.findByEmail(auth.getName());
        if (userOpt.isEmpty()) return ResponseEntity.status(404).body("User not found");

        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isEmpty()) return ResponseEntity.status(404).body("Doctor not found");

        User user = userOpt.get();
        Doctor doctor = doctorOpt.get();

        DoctorConsultation consultation = new DoctorConsultation();
        consultation.setUser(user);
        consultation.setDoctor(doctor);
        consultation.setIssueDescription(issueDescription);
        consultation.setStatus("REQUESTED");

        consultationRepository.save(consultation);

        return ResponseEntity.ok(Map.of(
                "message", "Doctor consultation requested",
                "consultationId", consultation.getId(),
                "doctorName", doctor.getName(),
                "specialization", doctor.getSpecialization(),
                "consultationFee", doctor.getConsultationFee()
        ));
    }

    // ✅ Get My Consultations (all)
    @GetMapping("/my-consultations")
    public ResponseEntity<?> myConsultations(Authentication auth) {
        Optional<User> userOpt = userRepository.findByEmail(auth.getName());
        if (userOpt.isEmpty()) return ResponseEntity.status(404).body("User not found");

        User user = userOpt.get();
        List<DoctorConsultation> consultations = consultationRepository.findByUserId(user.getId());

        // Map to DTO
        List<ConsultationResponseDTO> response = consultations.stream().map(c -> new ConsultationResponseDTO(
                c.getId(),
                c.getIssueDescription(),
                c.getStatus(),
                new ConsultationResponseDTO.DoctorInfo(
                        c.getDoctor().getId(),
                        c.getDoctor().getName(),
                        c.getDoctor().getSpecialization(),
                        c.getDoctor().getConsultationFee()
                ),
                c.getRequestedAt()
        )).toList();

        return ResponseEntity.ok(response);
    }

    // ✅ Get Consultations by Status
    @GetMapping("/my-consultations/status/{status}")
    public ResponseEntity<?> myConsultationsByStatus(Authentication auth,
                                                     @PathVariable String status) {
        Optional<User> userOpt = userRepository.findByEmail(auth.getName());
        if (userOpt.isEmpty()) return ResponseEntity.status(404).body("User not found");

        User user = userOpt.get();
        List<DoctorConsultation> consultations =
                consultationRepository.findByUserIdAndStatus(user.getId(), status.toUpperCase());

        return ResponseEntity.ok(consultations);
    }
}
