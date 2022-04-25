package com.prescription.management.service;

import com.prescription.management.entities.Patient;

public interface PatientService {
    Patient findByReferenceNumber(String patientReferenceNumber);
}
