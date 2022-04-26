package com.prescription.management.service;

import com.prescription.management.dto.request.AddPrescriptionRequest;
import com.prescription.management.dto.request.PageRequest;
import com.prescription.management.dto.response.ApiResponse;
import com.prescription.management.dto.response.PageResponse;
import com.prescription.management.dto.response.PrescriptionResponse;
import com.prescription.management.entities.Prescription;

public interface PrescriptionService {

    ApiResponse<PrescriptionResponse> getPrescription(String prescriptionReferenceNumber);

    PageResponse<PrescriptionResponse> getPrescriptions(PageRequest pageRequest) throws Exception;

    Prescription findByReferenceNumber(String prescriptionReferenceNumber);

    PageResponse<PrescriptionResponse> getPrescriptionsOfPatient(String patientReferenceNumber, PageRequest pageRequest) throws Exception;

    PageResponse<PrescriptionResponse> getPrescriptionsByDoctor(String doctorReferenceNumber, PageRequest pageRequest) throws Exception;

    ApiResponse create(String doctorReferenceNumber, String patientReferenceNumber, AddPrescriptionRequest addPrescriptionRequest) throws Exception;

    PrescriptionResponse map(Prescription prescriptionAccess);
}
