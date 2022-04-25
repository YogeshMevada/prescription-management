package com.prescription.management.repository;

import com.prescription.management.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Patient findByPatientReferenceNumber(String patientReferenceNumber);
}
