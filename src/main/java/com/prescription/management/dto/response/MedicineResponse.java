package com.prescription.management.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MedicineResponse {

    private long medicineId;

    private String medicineReferenceNumber;

    private int quantity;
}
