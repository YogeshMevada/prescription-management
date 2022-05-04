package com.prescription.management.repository;

import com.prescription.management.entities.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    Prescription findByPrescriptionReferenceNumber(String prescriptionReferenceNumber);

    @Query("FROM Prescription p where p.doctor.doctorReferenceNumber = ?1")
    Page<Prescription> findByDoctorReferenceNumber(String doctorReferenceNumber, Pageable pageable);

    @Query("FROM Prescription p where p.patient.patientReferenceNumber = ?1")
    Page<Prescription> findByPatientReferenceNumber(String patientReferenceNumber, Pageable pageable);
}
