package com.prescription.management.repository;

import com.prescription.management.entities.Pharmacist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PharmacistRepository extends JpaRepository<Pharmacist, Long> {

    Pharmacist findBypharmacyReferenceNumber(String pharmacyReferenceNumber);
}
