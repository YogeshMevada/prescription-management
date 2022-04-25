package com.prescription.management.service;

import com.prescription.management.dto.request.AddMedicineRequest;
import com.prescription.management.dto.response.ApiResponse;
import com.prescription.management.dto.response.MedicineResponse;
import com.prescription.management.entities.Medicine;

import java.util.List;

public interface MedicineService {
    MedicineResponse getMedicine(String medicineReferenceNumber) throws Exception;

    List<MedicineResponse> getMedicines() throws Exception;

    Medicine findMedicine(long medicineId, String medicineReferenceNumber);

    ApiResponse create(AddMedicineRequest addMedicineRequest) throws Exception;
}
