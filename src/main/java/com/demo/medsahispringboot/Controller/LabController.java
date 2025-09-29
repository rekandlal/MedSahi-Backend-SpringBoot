package com.demo.medsahispringboot.Controller;

import com.demo.medsahispringboot.Entity.LabTest;
import com.demo.medsahispringboot.Entity.User;
import com.demo.medsahispringboot.Repository.LabTestRepository;
import com.demo.medsahispringboot.Repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/lab")
public class LabController {

    private final LabTestRepository labTestRepository;
    private final UserRepository userRepository;

    public LabController(LabTestRepository labTestRepository, UserRepository userRepository) {
        this.labTestRepository = labTestRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/request")
    public ResponseEntity<?> requestLabTest(Authentication auth,
                                            @RequestParam String testName) {
        Optional<User> userOpt = userRepository.findByEmail(auth.getName());
        if (userOpt.isEmpty()) return ResponseEntity.status(404).body("User not found");
        User user = userOpt.get();

        LabTest labTest = new LabTest();
        labTest.setUser(user);
        labTest.setTestName(testName);
        labTest.setStatus("REQUESTED");

        labTestRepository.save(labTest);
        return ResponseEntity.ok(Map.of(
                "message", "Lab test requested",
                "testId", labTest.getId()
        ));
    }

    @GetMapping("/my-tests")
    public ResponseEntity<?> myLabTests(Authentication auth) {
        Optional<User> userOpt = userRepository.findByEmail(auth.getName());
        if (userOpt.isEmpty()) return ResponseEntity.status(404).body("User not found");
        User user = userOpt.get();

        List<LabTest> tests = labTestRepository.findByUserId(user.getId());
        return ResponseEntity.ok(tests);
    }
}

