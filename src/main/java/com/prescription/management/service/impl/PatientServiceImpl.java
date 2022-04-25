package com.prescription.management.service.impl;

import com.prescription.management.entities.Patient;
import com.prescription.management.repository.PatientRepository;
import com.prescription.management.service.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientServiceImpl(final PatientRepository patientRepository) {
        this.patientRepository = patientRepository;

    }

    @Override
    public Patient findByReferenceNumber(final String patientReferenceNumber) {
        log.info("Patient service - find by reference number");
        return patientRepository.findByPatientReferenceNumber(patientReferenceNumber);
    }
}
