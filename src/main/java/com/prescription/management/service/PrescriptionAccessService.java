package com.prescription.management.service;

import com.prescription.management.dto.response.ApiResponse;
import com.prescription.management.dto.response.PrescriptionAccessResponse;

import java.util.List;

public interface PrescriptionAccessService {

    List<PrescriptionAccessResponse> getPrescriptionAccessRequestsForPatient(String patientReferenceNumber) throws Exception;

    List<PrescriptionAccessResponse> getPrescriptionAccessRequestsByDoctor(String doctorReferenceNumber) throws Exception;

    List<PrescriptionAccessResponse> getPrescriptionAccessRequestsByPharmacist(String pharmacyReferenceNumber) throws Exception;

    ApiResponse requestPrescriptionByDoctor(String doctorReferenceNumber, String patientReferenceNumber, String prescriptionReferenceNumber) throws Exception;

    ApiResponse requestPrescriptionByPharmacist(String pharmacistReferenceNumber, String patientReferenceNumber, String prescriptionReferenceNumber) throws Exception;

    ApiResponse providePrescriptionAccess(String patientReferenceNumber, String prescriptionReferenceNumber) throws Exception;
}
