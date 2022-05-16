package com.prescription.commons.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class PrescriptionResponse {

    private String appointmentNumber;

    private String referenceNumber;

    private String doctorReferenceNumber;

    private String patientReferenceNumber;

    private List<MedicineResponse> medicines;
}
