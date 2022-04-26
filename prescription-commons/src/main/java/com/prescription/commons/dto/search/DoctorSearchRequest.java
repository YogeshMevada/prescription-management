package com.prescription.commons.dto.search;

import com.prescription.commons.dto.PageRequest;
import lombok.Data;

@Data
public class DoctorSearchRequest extends PageRequest {

    private String firstName;
    private String lastName;
    private String doctorReferenceNumber;
    private String degree;
}
