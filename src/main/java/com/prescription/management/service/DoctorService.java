package com.prescription.management.service;

import com.prescription.management.dto.request.UpdateDoctorRequest;
import com.prescription.management.dto.response.ApiResponse;
import com.prescription.management.entities.Doctor;

public interface DoctorService {
    Doctor findByReferenceNumber(String doctorReferenceNumber);

    ApiResponse update(UpdateDoctorRequest updateDoctorRequest);
}
