package com.prescription.management.service;

import com.prescription.commons.dto.request.AddMedicineRequest;
import com.prescription.commons.dto.response.ApiResponse;
import com.prescription.commons.dto.response.MedicineResponse;
import com.prescription.commons.dto.response.PageResponse;
import com.prescription.commons.dto.search.MedicineSearchRequest;
import com.prescription.management.entities.Medicine;

public interface MedicineService {
    ApiResponse<MedicineResponse> getMedicine(String medicineReferenceNumber) throws Exception;

    PageResponse<MedicineResponse> getMedicines(MedicineSearchRequest medicineSearchRequest) throws Exception;

    Medicine findMedicine(long medicineId, String medicineReferenceNumber);

    ApiResponse<MedicineResponse> create(AddMedicineRequest addMedicineRequest) throws Exception;
}
