package com.prescription.management.service;

import com.prescription.management.dto.request.AddPrescriptionRequest;
import com.prescription.management.dto.response.ApiResponse;
import com.prescription.management.dto.response.PrescriptionResponse;
import com.prescription.management.entities.Prescription;

import java.util.List;

public interface PrescriptionService {

    Prescription findByReferenceNumber(String prescriptionReferenceNumber);

    List<PrescriptionResponse> getPrescriptionsOfPatient(String patientReferenceNumber) throws Exception;

    List<PrescriptionResponse> getPrescriptionsByDoctor(String doctorReferenceNumber) throws Exception;

    List<PrescriptionResponse> getPrescriptionsOfPatientForDoctor(String doctorReferenceNumber, String patientReferenceNumber) throws Exception;

    ApiResponse create(String doctorReferenceNumber, String patientReferenceNumber, AddPrescriptionRequest addPrescriptionRequest) throws Exception;
}
