package com.prescription.management.service;

import com.prescription.management.entities.Medicine;

public interface MedicineService {
    Medicine findMedicine(long medicineId, String medicineReferenceNumber);
}
