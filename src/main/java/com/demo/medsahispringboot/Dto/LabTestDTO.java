package com.demo.medsahispringboot.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabTestDTO {
    private Long id;
    private String testName;
    private String status;
    private LocalDateTime requestedAt;
}

