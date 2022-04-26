package com.prescription.management.rest;

import com.prescription.management.dto.request.UpdateDoctorRequest;
import com.prescription.management.dto.response.ApiResponse;
import com.prescription.management.dto.response.DoctorResponse;
import com.prescription.management.dto.response.PageResponse;
import com.prescription.management.dto.search.DoctorSearchRequest;
import com.prescription.management.service.DoctorService;
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
public class DoctorController {

    private final DoctorService doctorService;

    @Autowired
    public DoctorController(final DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping(value = "/doctor/{doctorReferenceNumber}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<DoctorResponse>> getDoctor(@PathVariable @NotBlank(message = "Doctor reference number is mandatory") final String doctorReferenceNumber) {
        return ResponseEntity.ok(doctorService.getDoctor(doctorReferenceNumber));
    }

    @GetMapping(value = "/doctor", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<DoctorResponse>> getDoctors(@RequestBody @NotNull(message = "Page request is mandatory") final DoctorSearchRequest doctorSearchRequest) {
        return ResponseEntity.ok(doctorService.getDoctors(doctorSearchRequest));
    }

    @Secured({"ROLE_ADMIN", "ROLE_DOCTOR"})
    @PutMapping(value = "/doctor/{doctorReferenceNumber}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<DoctorResponse>> update(@PathVariable @NotBlank(message = "Doctor reference number is mandatory") final String doctorReferenceNumber,
                                                              @RequestBody @Valid final UpdateDoctorRequest updateDoctorRequest) {
        log.info("Doctor controller - update Doctor.");
        return ResponseEntity.ok(doctorService.update(doctorReferenceNumber, updateDoctorRequest));
    }
}
