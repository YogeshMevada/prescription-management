package com.prescription.management.rest;

import com.prescription.management.dto.request.AddPrescriptionRequest;
import com.prescription.management.dto.request.PageRequest;
import com.prescription.management.dto.response.ApiResponse;
import com.prescription.management.dto.response.PageResponse;
import com.prescription.management.dto.response.PrescriptionResponse;
import com.prescription.management.service.PrescriptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

    @Secured({"ROLE_ADMIN"})
    @GetMapping(value = "/prescription/{prescriptionReferenceNumber}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<PrescriptionResponse>> getPrescription(@PathVariable @NotBlank(message = "Prescription reference number is mandatory") final String prescriptionReferenceNumber) {
        log.info("Prescription controller - get all prescriptions");
        return ResponseEntity.ok(prescriptionService.getPrescription(prescriptionReferenceNumber));
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping(value = "/prescription", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<PrescriptionResponse>> getPrescriptions(@RequestBody @NotNull(message = "Page request is mandatory") final PageRequest pageRequest) throws Exception {
        log.info("Prescription controller - get all prescriptions");
        return ResponseEntity.ok(prescriptionService.getPrescriptions(pageRequest));
    }

    @Secured({"ROLE_ADMIN", "ROLE_PATIENT"})
    @GetMapping(value = "/patient/{patientReferenceNumber}/prescription", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<PrescriptionResponse>> getPrescriptionsOfPatient(@PathVariable @NotBlank(message = "Patient reference number is mandatory") final String patientReferenceNumber,
                                                                                        @RequestBody @NotNull(message = "Page request is mandatory") final PageRequest pageRequest) throws Exception {
        log.info("Prescription controller - get prescriptions of patient");
        return ResponseEntity.ok(prescriptionService.getPrescriptionsOfPatient(patientReferenceNumber, pageRequest));
    }

    @Secured({"ROLE_ADMIN", "ROLE_DOCTOR"})
    @GetMapping(value = "/doctor/{doctorReferenceNumber}/prescription", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<PrescriptionResponse>> getPrescriptionsByDoctor(@PathVariable @NotBlank(message = "Doctor reference number is mandatory") final String doctorReferenceNumber,
                                                                                       @RequestBody @NotNull(message = "Page request is mandatory") final PageRequest pageRequest) throws Exception {
        log.info("Prescription controller - get prescriptions by doctor");
        return ResponseEntity.ok(prescriptionService.getPrescriptionsByDoctor(doctorReferenceNumber, pageRequest));
    }

    @Secured({"ROLE_DOCTOR", "ROLE_PATIENT"})
    @PostMapping("/doctor/{doctorReferenceNumber}/patient/{patientReferenceNumber}/prescription")
    public ResponseEntity<ApiResponse<PrescriptionResponse>> create(@PathVariable("doctorReferenceNumber") @NotNull final String doctorReferenceNumber,
                                                                    @PathVariable("patientReferenceNumber") @NotNull final String patientReferenceNumber,
                                                                    @RequestBody @Valid AddPrescriptionRequest addPrescriptionRequest) throws Exception {
        log.info("Prescription controller - create");
        return ResponseEntity.ok(prescriptionService.create(doctorReferenceNumber, patientReferenceNumber, addPrescriptionRequest));
    }
}
