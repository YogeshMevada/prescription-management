package com.prescription.management.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddMedicineRequest {

    @NotBlank(message = "Brand name is mandatory")
    private String brandName;

    @NotBlank(message = "Active ingredient name is mandatory")
    private String activeIngredientName;

    @NotBlank(message = "Unit is mandatory")
    private String unit;
}
