package com.prescription.management.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
public class MedicineRequest {

    @Positive(message = "Medicine id can not be negative")
    private long medicineId;

    @NotBlank(message = "Medicine reference number is mandatory")
    private String medicineReferenceNumber;

    @Positive(message = "")
    private int quantity;

    @NotBlank(message = "Instruction is mandatory")
    private String instructions;
}
