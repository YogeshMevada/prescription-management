package com.prescription.management.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DoctorResponse {

    private String firstName;
    private String lastName;
    private String doctorReferenceNumber;
    private String degree;
}
