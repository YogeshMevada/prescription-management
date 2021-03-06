package com.prescription.management.service;

import com.prescription.commons.dto.PageRequest;
import com.prescription.commons.dto.request.AddPrescriptionRequest;
import com.prescription.commons.dto.response.ApiResponse;
import com.prescription.commons.dto.response.PageResponse;
import com.prescription.commons.dto.response.PrescriptionResponse;
import com.prescription.management.entities.Prescription;

public interface PrescriptionService {

    ApiResponse<PrescriptionResponse> getPrescription(String prescriptionReferenceNumber);

    PageResponse<PrescriptionResponse> getPrescriptions(PageRequest pageRequest) throws Exception;

    Prescription findByReferenceNumber(String prescriptionReferenceNumber);

    Prescription validateByReferenceNumber(String prescriptionReferenceNumber) throws Exception;

    PageResponse<PrescriptionResponse> getPrescriptionsOfPatient(String patientReferenceNumber, PageRequest pageRequest) throws Exception;

    PageResponse<PrescriptionResponse> getPrescriptionsByDoctor(String doctorReferenceNumber, PageRequest pageRequest) throws Exception;

    ApiResponse<PrescriptionResponse> create(String doctorReferenceNumber, String patientReferenceNumber, AddPrescriptionRequest addPrescriptionRequest) throws Exception;

    PrescriptionResponse map(Prescription prescriptionAccess);
}
