package com.prescription.commons.dto.search;

import com.prescription.commons.dto.PageRequest;
import lombok.Data;

@Data
public class MedicineSearchRequest extends PageRequest {

    private String medicineReferenceNumber;
    private String brandName;
    private String activeIngredientName;
}
