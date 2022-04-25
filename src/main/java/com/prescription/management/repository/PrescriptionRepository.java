package com.prescription.management.repository;

import com.prescription.management.entities.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    Prescription findByPrescriptionReferenceNumber(String prescriptionReferenceNumber);

    @Query("FROM Prescription p where p.doctor.doctorReferenceNumber = ?1")
    List<Prescription> findByDoctorReferenceNumber(String doctorReferenceNumber);

    @Query("FROM Prescription p where p.patient.patientReferenceNumber = ?1")
    List<Prescription> findByPatientReferenceNumber(String patientReferenceNumber);
}
