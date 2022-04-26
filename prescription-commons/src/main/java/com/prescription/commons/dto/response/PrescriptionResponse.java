package com.prescription.commons.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PrescriptionResponse {

    private String appointmentNumber;

    private String referenceNumber;

    private String doctorReferenceNumber;

    private String patientReferenceNumber;

    private List<MedicineResponse> medicines;
}
