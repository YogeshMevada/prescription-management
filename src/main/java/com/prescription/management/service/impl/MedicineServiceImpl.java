package com.prescription.management.service.impl;

import com.prescription.management.entities.Medicine;
import com.prescription.management.repository.MedicineRepository;
import com.prescription.management.service.MedicineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;

    @Autowired
    public MedicineServiceImpl(final MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    @Override
    public Medicine findMedicine(long medicineId, String medicineReferenceNumber) {
        log.info("Medicine service - find by medicine id & reference number");
        return medicineRepository.findByIdAndMedicineReferenceNumber(medicineId, medicineReferenceNumber);
    }
}
