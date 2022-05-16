package com.prescription.commons.dto.response;

import lombok.Data;

@Data
public class PrescriptionAccessResponse {

    private String accessReferenceNumber;

    private String doctorReferenceNumber;

    private String pharmacyReferenceNumber;

    private String patientReferenceNumber;

    private String prescriptionReferenceNumber;

    private boolean approved = false;
}
