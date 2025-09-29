package com.demo.medsahispringboot.Controller;

import com.demo.medsahispringboot.Entity.DoctorConsultation;
import com.demo.medsahispringboot.Entity.User;
import com.demo.medsahispringboot.Repository.DoctorConsultationRepository;
import com.demo.medsahispringboot.Repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    private final DoctorConsultationRepository consultationRepository;
    private final UserRepository userRepository;

    public DoctorController(DoctorConsultationRepository consultationRepository,
                            UserRepository userRepository) {
        this.consultationRepository = consultationRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/request")
    public ResponseEntity<?> requestConsultation(Authentication auth,
                                                 @RequestParam String issueDescription) {
        Optional<User> userOpt = userRepository.findByEmail(auth.getName());
        if (userOpt.isEmpty()) return ResponseEntity.status(404).body("User not found");
        User user = userOpt.get();

        DoctorConsultation consultation = new DoctorConsultation();
        consultation.setUser(user);
        consultation.setIssueDescription(issueDescription);
        consultation.setStatus("REQUESTED");

        consultationRepository.save(consultation);
        return ResponseEntity.ok(Map.of(
                "message", "Doctor consultation requested",
                "consultationId", consultation.getId()
        ));
    }

    @GetMapping("/my-consultations")
    public ResponseEntity<?> myConsultations(Authentication auth) {
        Optional<User> userOpt = userRepository.findByEmail(auth.getName());
        if (userOpt.isEmpty()) return ResponseEntity.status(404).body("User not found");
        User user = userOpt.get();

        List<DoctorConsultation> consultations = consultationRepository.findByUserId(user.getId());
        return ResponseEntity.ok(consultations);
    }
}

