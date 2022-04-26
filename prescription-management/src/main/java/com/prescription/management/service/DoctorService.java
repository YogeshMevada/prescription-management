package com.prescription.management.service;

import com.prescription.commons.dto.request.UpdateDoctorRequest;
import com.prescription.commons.dto.response.ApiResponse;
import com.prescription.commons.dto.response.DoctorResponse;
import com.prescription.commons.dto.response.PageResponse;
import com.prescription.commons.dto.search.DoctorSearchRequest;
import com.prescription.management.entities.Doctor;

public interface DoctorService {
    Doctor findByReferenceNumber(String doctorReferenceNumber);

    ApiResponse<DoctorResponse> getDoctor(String doctorReferenceNumber);

    PageResponse<DoctorResponse> getDoctors(DoctorSearchRequest doctorSearchRequest);

    Doctor save(Doctor doctor);

    ApiResponse<DoctorResponse> update(String doctorReferenceNumber, UpdateDoctorRequest updateDoctorRequest);
}
