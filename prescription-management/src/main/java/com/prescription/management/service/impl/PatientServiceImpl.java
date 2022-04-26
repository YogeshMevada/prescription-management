package com.prescription.management.service.impl;

import com.prescription.management.entities.Patient;
import com.prescription.management.repository.PatientRepository;
import com.prescription.management.service.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

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

    @Override
    public Patient validateByReferenceNumber(final String patientReferenceNumber) throws Exception {
        final Patient patient = findByReferenceNumber(patientReferenceNumber);
        if (Objects.isNull(patient)) {
            log.error("Invalid Patient reference number : {}", patientReferenceNumber);
            throw new Exception("Invalid Patient reference number");
        }
        return patient;
    }

    @Override
    public Patient save(final Patient patient) {
        return patientRepository.save(patient);
    }
}
