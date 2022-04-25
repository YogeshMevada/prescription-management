package com.prescription.management.dto.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AddMedicineRequest {

    @NotBlank(message = "Brand name is mandatory")
    private String brandName;

    @NotBlank(message = "Active ingredient name is mandatory")
    private String activeIngredientName;

    @Min(value = 1, message = "Quantity is mandatory")
    private long quantity;

    @NotNull(message = "Unit is mandatory")
    private String unit;
}
