package com.prescription.management.service;

import com.prescription.management.entities.Patient;

public interface PatientService {
    Patient findByReferenceNumber(String patientReferenceNumber);

    Patient validateByReferenceNumber(String patientReferenceNumber) throws Exception;

    Patient save(Patient patient);
}
