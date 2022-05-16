package com.prescription.commons.dto.response;

import lombok.Data;

@Data
public class DoctorResponse {

    private String firstName;
    private String lastName;
    private String doctorReferenceNumber;
    private String degree;
}
