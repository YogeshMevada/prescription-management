package com.prescription.management.dto.search;

import com.prescription.management.dto.PageRequest;
import lombok.Data;

@Data
public class MedicineSearchRequest extends PageRequest {

    private String medicineReferenceNumber;
    private String brandName;
    private String activeIngredientName;
}
