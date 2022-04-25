package com.prescription.management.rest;

import com.prescription.management.dto.request.AddMedicineRequest;
import com.prescription.management.dto.response.ApiResponse;
import com.prescription.management.dto.response.MedicineResponse;
import com.prescription.management.service.MedicineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1")
public class MedicineController {

    private final MedicineService medicineService;

    @Autowired
    public MedicineController(final MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @Secured({"ROLE_ADMIN", "ROLE_DOCTOR"})
    @GetMapping(value = "/medicine/{medicineReferenceNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MedicineResponse> getMedicine(final String medicineReferenceNumber) throws Exception {
        log.info("Medicine controller - create medicine");
        return ResponseEntity.ok(medicineService.getMedicine(medicineReferenceNumber));
    }

    @Secured({"ROLE_ADMIN", "ROLE_DOCTOR"})
    @GetMapping(value = "/medicine", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MedicineResponse>> getMedicines() throws Exception {
        log.info("Medicine controller - create medicine");
        return ResponseEntity.ok(medicineService.getMedicines());
    }

    @Secured({"ROLE_ADMIN", "ROLE_DOCTOR"})
    @PostMapping(value = "/medicine", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> create(@RequestBody @Valid final AddMedicineRequest addMedicineRequest) throws Exception {
        log.info("Medicine controller - create medicine");
        return ResponseEntity.ok(medicineService.create(addMedicineRequest));
    }
}
