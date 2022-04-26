package com.prescription.management.service;

import com.prescription.commons.dto.PageRequest;
import com.prescription.commons.dto.response.ApiResponse;
import com.prescription.commons.dto.response.PageResponse;
import com.prescription.commons.dto.response.PrescriptionAccessResponse;
import com.prescription.commons.dto.response.PrescriptionResponse;

public interface PrescriptionAccessService {

    PageResponse<PrescriptionAccessResponse> getPrescriptionAccessRequestsForPatient(String patientReferenceNumber, PageRequest pageRequest) throws Exception;

    PageResponse<PrescriptionAccessResponse> getPrescriptionAccessRequestsByDoctor(String doctorReferenceNumber, PageRequest pageRequest) throws Exception;

    PageResponse<PrescriptionAccessResponse> getPrescriptionAccessRequestsByPharmacist(String pharmacyReferenceNumber, PageRequest pageRequest) throws Exception;

    ApiResponse<PrescriptionAccessResponse> requestPrescriptionByDoctor(String doctorReferenceNumber, String patientReferenceNumber, String prescriptionReferenceNumber) throws Exception;

    ApiResponse<PrescriptionAccessResponse> requestPrescriptionByPharmacist(String pharmacistReferenceNumber, String patientReferenceNumber, String prescriptionReferenceNumber) throws Exception;

    ApiResponse<PrescriptionAccessResponse> providePrescriptionAccess(String patientReferenceNumber, String prescriptionReferenceNumber) throws Exception;

    PrescriptionResponse getApprovedPrescription(String prescriptionReferenceNumber, String accessReferenceNumber) throws Exception;
}
