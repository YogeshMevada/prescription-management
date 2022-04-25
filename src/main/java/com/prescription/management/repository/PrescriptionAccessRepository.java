package com.prescription.management.repository;

import com.prescription.management.entities.PrescriptionAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionAccessRepository extends JpaRepository<PrescriptionAccess, Long> {

    @Query("FROM PrescriptionAccess pa WHERE pa.patient.patientReferenceNumber = ?1")
    List<PrescriptionAccess> findByPatientReferenceNumber(String patientReferenceNumber);

    @Query("FROM PrescriptionAccess pa WHERE pa.doctor.doctorReferenceNumber = ?1")
    List<PrescriptionAccess> findByDoctorReferenceNumber(String doctorReferenceNumber);

    @Query("FROM PrescriptionAccess pa WHERE pa.pharmacist.pharmacyReferenceNumber = ?1")
    List<PrescriptionAccess> findByPharmacyReferenceNumber(String pharmacyReferenceNumber);

    @Query("FROM PrescriptionAccess pa WHERE pa.pharmacist.pharmacyReferenceNumber = ?1 AND pa.prescription.prescriptionReferenceNumber = ?2 AND pa.approved = ?3")
    PrescriptionAccess findByPharmacyReferenceNumberAndPrescriptionReferenceNumber(String pharmacyReferenceNumber, String prescriptionReferenceNumber, boolean approved);

    PrescriptionAccess findByAccessReferenceNumber(String accessReferenceNumber);
}
