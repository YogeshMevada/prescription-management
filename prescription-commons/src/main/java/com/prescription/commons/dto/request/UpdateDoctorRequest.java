package com.prescription.commons.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
public class UpdateDoctorRequest {

    @Positive(message = "Doctor id must be greater than zero")
    private long doctorId;

    @NotBlank(message = "Role name is mandatory")
    private String doctorReferenceNumber;

    private String degree;
}
