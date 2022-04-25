package com.prescription.management.rest;

import com.prescription.management.dto.request.UpdateDoctorRequest;
import com.prescription.management.dto.response.ApiResponse;
import com.prescription.management.service.DoctorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("api/v1")
public class DoctorController {

    private final DoctorService doctorService;

    @Autowired
    public DoctorController(final DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @Secured({"ROLE_ADMIN", "DOCTOR"})
    @PutMapping(value = "/doctor/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> update(@RequestBody @Valid final UpdateDoctorRequest updateDoctorRequest) {
        log.info("Doctor controller - update Doctor.");
        return ResponseEntity.ok(doctorService.update(updateDoctorRequest));
    }
}
