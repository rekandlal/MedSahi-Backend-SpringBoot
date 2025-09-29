package com.demo.medsahispringboot.Dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicineDto {
    private String brandedName;
    private String ingredient;
    private String dosage;
    private String manufacturer;
    private Double mrp;
    private Double finalPrice;
    private String form;
    private List<GenericDto> generics;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GenericDto {
        private String name;
        private String ingredient;
        private String dosage;
        private String manufacturer;
        private Double mrp;
        private Double finalPrice;
        private String form;
    }
}

