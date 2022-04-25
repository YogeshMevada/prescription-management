package com.prescription.management.repository;

import com.prescription.management.entities.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    Medicine findByIdAndMedicineReferenceNumber(long medicineId, String medicineReferenceNumber);
}
