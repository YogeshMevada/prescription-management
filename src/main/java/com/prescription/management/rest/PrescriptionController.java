package com.prescription.management.rest;

import com.prescription.management.dto.request.AddPrescriptionRequest;
import com.prescription.management.dto.response.ApiResponse;
import com.prescription.management.dto.response.PrescriptionResponse;
import com.prescription.management.service.PrescriptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("api/v1")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @Autowired
    public PrescriptionController(final PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @Secured({"ROLE_ADMIN", "ROLE_PATIENT"})
    @GetMapping("/patient/{patientReferenceNumber}/prescription")
    public ResponseEntity<List<PrescriptionResponse>> getPrescriptionsOfPatient(@PathVariable @NotBlank(message = "Patient reference number is mandatory") final String patientReferenceNumber) throws Exception {
        log.info("Prescription controller - get prescriptions of patient");
        return ResponseEntity.ok(prescriptionService.getPrescriptionsOfPatient(patientReferenceNumber));
    }

    @Secured({"ROLE_ADMIN", "ROLE_DOCTOR"})
    @GetMapping("/doctor/{doctorReferenceNumber}/prescription")
    public ResponseEntity<List<PrescriptionResponse>> getPrescriptionsByDoctor(@PathVariable @NotBlank(message = "Doctor reference number is mandatory") final String doctorReferenceNumber) throws Exception {
        log.info("Prescription controller - get prescriptions by doctor");
        return ResponseEntity.ok(prescriptionService.getPrescriptionsByDoctor(doctorReferenceNumber));
    }

    @Secured({"ROLE_ADMIN", "ROLE_DOCTOR"})
    @GetMapping("/doctor/{doctorReferenceNumber}/patient/{patientReferenceNumber}/prescription")
    public ResponseEntity<List<PrescriptionResponse>> getPrescriptionsOfPatientForDoctor(@PathVariable @NotBlank(message = "Doctor reference number is mandatory") final String doctorReferenceNumber,
                                                                                         @PathVariable @NotBlank(message = "Patient reference number is mandatory") final String patientReferenceNumber) throws Exception {
        log.info("Prescription controller - get prescriptions of patient for doctor");
        return ResponseEntity.ok(prescriptionService.getPrescriptionsOfPatientForDoctor(doctorReferenceNumber, patientReferenceNumber));
    }

    @Secured({"ROLE_DOCTOR"})
    @PostMapping("/doctor/{doctorReferenceNumber}/patient/{patientReferenceNumber}/prescription")
    public ResponseEntity<ApiResponse> create(@PathVariable("doctorReferenceNumber") @NotNull final String doctorReferenceNumber,
                                              @PathVariable("patientReferenceNumber") @NotNull final String patientReferenceNumber,
                                              @RequestBody @Valid AddPrescriptionRequest addPrescriptionRequest) throws Exception {
        log.info("Prescription controller - create");
        return ResponseEntity.ok(prescriptionService.create(doctorReferenceNumber, patientReferenceNumber, addPrescriptionRequest));
    }
}
