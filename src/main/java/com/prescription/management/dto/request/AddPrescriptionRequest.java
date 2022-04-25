package com.prescription.management.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class AddPrescriptionRequest {

    @NotEmpty(message = "Appointment number is mandatory!")
    private String appointmentNumber;

    @Size(min = 1)
    private List<MedicineRequest> medicines;
}
