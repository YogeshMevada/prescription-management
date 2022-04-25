package com.prescription.management.service.impl;

import com.prescription.management.dto.request.UpdateDoctorRequest;
import com.prescription.management.dto.response.ApiResponse;
import com.prescription.management.entities.Doctor;
import com.prescription.management.repository.DoctorRepository;
import com.prescription.management.service.DoctorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    @Autowired
    public DoctorServiceImpl(final DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public Doctor findByReferenceNumber(final String doctorReferenceNumber) {
        log.info("Doctor service - find By ReferenceNumber");
        return doctorRepository.findByDoctorReferenceNumber(doctorReferenceNumber);
    }

    @Override
    public ApiResponse update(final UpdateDoctorRequest updateDoctorRequest) {
        log.info("Doctor service - update");
        return null;
    }
}
