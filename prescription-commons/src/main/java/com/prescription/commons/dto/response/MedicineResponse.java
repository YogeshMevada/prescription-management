package com.prescription.commons.dto.response;

import lombok.Data;

@Data
public class MedicineResponse {

    private long medicineId;

    private String medicineReferenceNumber;

    private String brandName;

    private String activeIngredientName;

    private long quantity;

    private String unit;
}
