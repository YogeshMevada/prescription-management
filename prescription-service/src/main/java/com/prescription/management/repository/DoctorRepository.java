package com.prescription.management.repository;

import com.prescription.management.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long>, JpaSpecificationExecutor<Doctor> {
    Doctor findByDoctorReferenceNumber(String doctorReferenceNumber);
}
