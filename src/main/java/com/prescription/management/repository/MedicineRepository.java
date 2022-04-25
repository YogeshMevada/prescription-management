package com.prescription.management.repository;

import com.prescription.management.entities.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    Medicine findByMedicineReferenceNumber(String medicineReferenceNumber);

    Medicine findByIdAndMedicineReferenceNumber(long medicineId, String medicineReferenceNumber);

    @Query("FROM Medicine m WHERE m.activeIngredientName = activeIngredientName OR m.brandName = ?2")
    Medicine findDuplicate(String activeIngredientName, String brandName);
}
