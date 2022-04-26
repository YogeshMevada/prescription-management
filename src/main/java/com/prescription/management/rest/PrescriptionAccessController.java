package com.prescription.management.rest;

import com.prescription.management.dto.PageRequest;
import com.prescription.management.dto.response.ApiResponse;
import com.prescription.management.dto.response.PageResponse;
import com.prescription.management.dto.response.PrescriptionAccessResponse;
import com.prescription.management.dto.response.PrescriptionResponse;
import com.prescription.management.service.PrescriptionAccessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@Slf4j
@Validated
@RestController
@RequestMapping("api/v1")
public class PrescriptionAccessController {

    private final PrescriptionAccessService prescriptionAccessService;

    @Autowired
    public PrescriptionAccessController(final PrescriptionAccessService prescriptionAccessService) {
        this.prescriptionAccessService = prescriptionAccessService;
    }

    @Secured({"ROLE_ADMIN", "ROLE_PATIENT"})
    @GetMapping(value = "/patient/{patientReferenceNumber}/access", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<PrescriptionAccessResponse>> getPrescriptionAccessRequestsForPatient(@PathVariable("patientReferenceNumber") @NotNull(message = "Doctor reference number is mandatory") final String patientReferenceNumber,
                                                                                                            @RequestBody @NotNull(message = "Page request is mandatory") final PageRequest pageRequest) throws Exception {
        log.info("Prescription access controller - get prescription access requests by doctor");
        return ResponseEntity.ok(prescriptionAccessService.getPrescriptionAccessRequestsForPatient(patientReferenceNumber, pageRequest));
    }

    @Secured({"ROLE_ADMIN", "ROLE_DOCTOR"})
    @GetMapping(value = "/doctor/{doctorReferenceNumber}/access", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<PrescriptionAccessResponse>> getPrescriptionAccessRequestsByDoctor(@PathVariable("doctorReferenceNumber") @NotNull(message = "Doctor reference number is mandatory") final String doctorReferenceNumber,
                                                                                                          @RequestBody @NotNull(message = "Page request is mandatory") final PageRequest pageRequest) throws Exception {
        log.info("Prescription access controller - get prescription access requests by doctor");
        return ResponseEntity.ok(prescriptionAccessService.getPrescriptionAccessRequestsByDoctor(doctorReferenceNumber, pageRequest));
    }

    @Secured({"ROLE_ADMIN", "ROLE_PHARMACIST"})
    @GetMapping(value = "/pharmacist/{pharmacyReferenceNumber}/access", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<PrescriptionAccessResponse>> getPrescriptionAccessRequestsByPharmacist(@PathVariable("pharmacyReferenceNumber") @NotNull(message = "Pharmacy reference number is mandatory") final String pharmacyReferenceNumber,
                                                                                                              @RequestBody @NotNull(message = "Page request is mandatory") final PageRequest pageRequest) throws Exception {
        log.info("Prescription access controller - get prescription access requests by doctor");
        return ResponseEntity.ok(prescriptionAccessService.getPrescriptionAccessRequestsByPharmacist(pharmacyReferenceNumber, pageRequest));
    }

    @Secured({"ROLE_DOCTOR"})
    @PostMapping(value = "/doctor/{doctorReferenceNumber}/patient/{patientReferenceNumber}/prescription/{prescriptionReferenceNumber}/access",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> requestPrescriptionByDoctor(@PathVariable("doctorReferenceNumber") @NotNull(message = "Doctor reference number is mandatory") final String doctorReferenceNumber,
                                                                   @PathVariable("patientReferenceNumber") @NotNull(message = "Patient reference number is mandatory") final String patientReferenceNumber,
                                                                   @PathVariable("prescriptionReferenceNumber") @NotNull(message = "Prescription reference number is mandatory") final String prescriptionReferenceNumber) throws Exception {
        log.info("Prescription access controller - request prescription by doctor");
        return ResponseEntity.ok(prescriptionAccessService.requestPrescriptionByDoctor(doctorReferenceNumber, patientReferenceNumber, prescriptionReferenceNumber));
    }

    @Secured({"ROLE_PHARMACIST"})
    @PostMapping(value = "/pharmacist/{pharmacistReferenceNumber}/patient/{patientReferenceNumber}/prescription/{prescriptionReferenceNumber}/access",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> requestPrescriptionByPharmacist(@PathVariable("pharmacistReferenceNumber") @NotNull(message = "Doctor reference number is mandatory") final String pharmacistReferenceNumber,
                                                                       @PathVariable("patientReferenceNumber") @NotNull(message = "Patient reference number is mandatory") final String patientReferenceNumber,
                                                                       @PathVariable("prescriptionReferenceNumber") @NotNull(message = "Prescription reference number is mandatory") final String prescriptionReferenceNumber) throws Exception {
        log.info("Prescription access controller - request prescription by pharmacist");
        return ResponseEntity.ok(prescriptionAccessService.requestPrescriptionByPharmacist(pharmacistReferenceNumber, patientReferenceNumber, prescriptionReferenceNumber));
    }

    @Secured({"ROLE_PATIENT"})
    @PutMapping(value = "/patient/{patientReferenceNumber}/access/{accessReferenceNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> providePrescriptionAccess(@PathVariable("patientReferenceNumber") @NotNull(message = "Doctor reference number is mandatory") final String patientReferenceNumber,
                                                                 @PathVariable("accessReferenceNumber") @NotNull(message = "Prescription access reference number is mandatory") final String accessReferenceNumber) throws Exception {
        log.info("Prescription access controller - get prescription access requests by doctor");
        return ResponseEntity.ok(prescriptionAccessService.providePrescriptionAccess(patientReferenceNumber, accessReferenceNumber));
    }

    @Secured({"ROLE_DOCTOR", "ROLE_PHARMACIST"})
    @GetMapping(value = "/prescription/{prescriptionReferenceNumber}/access/{accessReferenceNumber}")
    public ResponseEntity<PrescriptionResponse> getApprovedPrescription(@PathVariable("prescriptionReferenceNumber") @NotNull(message = "Doctor reference number is mandatory") final String prescriptionReferenceNumber,
                                                                        @PathVariable("accessReferenceNumber") @NotNull(message = "Doctor reference number is mandatory") final String accessReferenceNumber) throws Exception {
        log.info("Prescription access controller - get approved prescription");
        return ResponseEntity.ok(prescriptionAccessService.getApprovedPrescription(prescriptionReferenceNumber, accessReferenceNumber));
    }
}
