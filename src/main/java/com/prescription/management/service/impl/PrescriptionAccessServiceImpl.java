package com.prescription.management.service.impl;

import com.prescription.management.constant.ErrorCode;
import com.prescription.management.constant.ResponseStatus;
import com.prescription.management.dto.response.ApiResponse;
import com.prescription.management.dto.response.PrescriptionAccessResponse;
import com.prescription.management.dto.response.PrescriptionResponse;
import com.prescription.management.entities.*;
import com.prescription.management.repository.PrescriptionAccessRepository;
import com.prescription.management.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PrescriptionAccessServiceImpl implements PrescriptionAccessService {

    private final PrescriptionAccessRepository prescriptionAccessRepository;

    private final DoctorService doctorService;

    private final PharmacistService pharmacistService;

    private final PatientService patientService;

    private final PrescriptionService prescriptionService;

    @Autowired
    public PrescriptionAccessServiceImpl(final PrescriptionAccessRepository prescriptionAccessRepository,
                                         final DoctorService doctorService,
                                         final PharmacistService pharmacistService,
                                         final PatientService patientService,
                                         final PrescriptionService prescriptionService) {
        this.prescriptionAccessRepository = prescriptionAccessRepository;
        this.doctorService = doctorService;
        this.pharmacistService = pharmacistService;
        this.patientService = patientService;
        this.prescriptionService = prescriptionService;
    }

    @Override
    public List<PrescriptionAccessResponse> getPrescriptionAccessRequestsForPatient(final String patientReferenceNumber) throws Exception {
        log.info("Prescription service - get prescription access requests for patient");
        final Patient patient = patientService.findByReferenceNumber(patientReferenceNumber);
        if (Objects.isNull(patient)) {
            log.error("Invalid Patient reference number : {}", patientReferenceNumber);
            throw new Exception("Invalid Patient reference number");
        }
        final List<PrescriptionAccess> prescriptionAccesses = prescriptionAccessRepository.findByPatientReferenceNumber(patientReferenceNumber, false);
        return prescriptionAccesses.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<PrescriptionAccessResponse> getPrescriptionAccessRequestsByDoctor(final String doctorReferenceNumber) throws Exception {
        log.info("Prescription service - get prescription access requests by doctor");
        final Doctor doctor = doctorService.findByReferenceNumber(doctorReferenceNumber);
        if (Objects.isNull(doctor)) {
            log.error("Invalid Doctor reference number : {}", doctorReferenceNumber);
            throw new Exception("Invalid Doctor reference number");
        }
        final List<PrescriptionAccess> prescriptionAccesses = prescriptionAccessRepository.findByDoctorReferenceNumber(doctorReferenceNumber, false);
        return prescriptionAccesses.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<PrescriptionAccessResponse> getPrescriptionAccessRequestsByPharmacist(final String pharmacyReferenceNumber) throws Exception {
        log.info("Prescription service - get prescription access requests by pharmacist");
        final Pharmacist pharmacist = pharmacistService.findByReferenceNumber(pharmacyReferenceNumber);
        if (Objects.isNull(pharmacist)) {
            log.error("Invalid Pharmacist reference number : {}", pharmacyReferenceNumber);
            throw new Exception("Invalid Pharmacist reference number");
        }
        final List<PrescriptionAccess> prescriptionAccesses = prescriptionAccessRepository.findByPharmacyReferenceNumber(pharmacyReferenceNumber, false);
        return prescriptionAccesses.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ApiResponse requestPrescriptionByDoctor(final String doctorReferenceNumber, final String patientReferenceNumber, final String prescriptionReferenceNumber) throws Exception {
        log.info("Prescription service - get prescriptions of patient for doctor");
        final Doctor doctor = doctorService.findByReferenceNumber(doctorReferenceNumber);
        if (Objects.isNull(doctor)) {
            log.error("Invalid Doctor reference number : {}", doctorReferenceNumber);
            throw new Exception("Invalid Doctor reference number");
        }
        final Patient patient = patientService.findByReferenceNumber(patientReferenceNumber);
        if (Objects.isNull(patient)) {
            log.error("Invalid Patient reference number : {}", patientReferenceNumber);
            throw new Exception("Invalid patient reference number");
        }
        final Prescription prescription = prescriptionService.findByReferenceNumber(prescriptionReferenceNumber);
        if (Objects.isNull(prescription)) {
            log.error("Invalid Prescription reference number : {}", prescriptionReferenceNumber);
            throw new Exception("Invalid prescription reference number");
        }
        if (doctorReferenceNumber.equals(prescription.getDoctor().getDoctorReferenceNumber())) {
            return ApiResponse.builder()
                    .message("You already have the access to the prescription")
                    .status(ResponseStatus.SUCCESS)
                    .build();
        }
        if (patientReferenceNumber.equals(prescription.getPatient().getPatientReferenceNumber())) {
            log.error("Prescription reference number-{} does not belong to patient-{}", prescriptionReferenceNumber, patientReferenceNumber);
            throw new Exception("Invalid prescription reference number for patient");
        }

        final PrescriptionAccess prescriptionAccess = new PrescriptionAccess();
        prescriptionAccess.setAccessReferenceNumber(UUID.randomUUID().toString());
        prescriptionAccess.setDoctor(doctor);
        prescriptionAccess.setPatient(patient);
        prescriptionAccess.setPrescription(prescription);
        prescriptionAccess.setApproved(false);
        prescriptionAccessRepository.save(prescriptionAccess);

        log.info("Prescription successfully requested from patient");
        return ApiResponse.builder()
                .message("Prescription successfully requested from patient")
                .status(ResponseStatus.SUCCESS)
                .build();
    }

    @Override
    @Transactional
    public ApiResponse requestPrescriptionByPharmacist(final String pharmacyReferenceNumber, final String patientReferenceNumber, final String prescriptionReferenceNumber) throws Exception {
        log.info("Prescription service - get prescriptions of patient for pharmacist");
        final Pharmacist pharmacist = pharmacistService.findByReferenceNumber(pharmacyReferenceNumber);
        if (Objects.isNull(pharmacist)) {
            log.error("Invalid Pharmacy reference number : {}", pharmacyReferenceNumber);
            throw new Exception("Invalid Pharmacy reference number");
        }
        final PrescriptionAccess prescriptionAccessFound = prescriptionAccessRepository.findByPharmacyReferenceNumberAndPrescriptionReferenceNumber(pharmacyReferenceNumber, prescriptionReferenceNumber, false);
        if (Objects.nonNull(prescriptionAccessFound)) {
            return ApiResponse.builder()
                    .message("You already have the access to the prescription")
                    .status(ResponseStatus.SUCCESS)
                    .build();
        }

        final Patient patient = patientService.findByReferenceNumber(patientReferenceNumber);
        if (Objects.isNull(patient)) {
            log.error("Invalid Patient reference number : {}", patientReferenceNumber);
            throw new Exception("Invalid patient reference number");
        }
        final Prescription prescription = prescriptionService.findByReferenceNumber(prescriptionReferenceNumber);
        if (Objects.isNull(prescription)) {
            log.error("Invalid Prescription reference number : {}", prescriptionReferenceNumber);
            throw new Exception("Invalid prescription reference number");
        }

        if (patientReferenceNumber.equals(prescription.getPatient().getPatientReferenceNumber())) {
            log.error("Prescription reference number-{} does not belong to patient-{}", prescriptionReferenceNumber, patientReferenceNumber);
            throw new Exception("Invalid prescription reference number for patient");
        }

        final PrescriptionAccess prescriptionAccess = new PrescriptionAccess();
        prescriptionAccess.setAccessReferenceNumber(UUID.randomUUID().toString());
        prescriptionAccess.setPharmacist(pharmacist);
        prescriptionAccess.setPatient(patient);
        prescriptionAccess.setPrescription(prescription);
        prescriptionAccess.setApproved(false);
        prescriptionAccessRepository.save(prescriptionAccess);

        log.info("Prescription successfully requested from patient");
        return ApiResponse.builder()
                .message("Prescription successfully requested from patient")
                .status(ResponseStatus.SUCCESS)
                .build();
    }

    @Override
    @Transactional
    public ApiResponse providePrescriptionAccess(final String patientReferenceNumber, final String accessReferenceNumber) throws Exception {
        log.info("Prescription service - provide prescription access");
        final Patient patient = patientService.findByReferenceNumber(patientReferenceNumber);
        if (Objects.isNull(patient)) {
            log.error("Invalid Patient reference number : {}", patientReferenceNumber);
            throw new Exception("Invalid patient reference number");
        }
        final PrescriptionAccess prescriptionAccess = prescriptionAccessRepository.findByAccessReferenceNumber(accessReferenceNumber);
        if (Objects.isNull(prescriptionAccess)) {
            log.error("Invalid Prescription access reference number : {}", accessReferenceNumber);
            throw new Exception("Invalid Prescription access reference number");
        }
        if (patientReferenceNumber.equals(prescriptionAccess.getPatient().getPatientReferenceNumber())) {
            log.error("Invalid prescription-{} for Patient-{}", prescriptionAccess.getPrescription().getPrescriptionReferenceNumber(), accessReferenceNumber);
            return ApiResponse.builder()
                    .message("Invalid prescription for Patient")
                    .status(ResponseStatus.ERROR)
                    .errorCode(ErrorCode.PA001)
                    .build();
        }

        prescriptionAccess.setApproved(true);
        prescriptionAccessRepository.save(prescriptionAccess);

        log.info("Prescription access provided successfully");
        return ApiResponse.builder()
                .message("Prescription access provided successfully")
                .status(ResponseStatus.SUCCESS)
                .build();
    }

    @Override
    public PrescriptionResponse getApprovedPrescription(final String prescriptionReferenceNumber, final String accessReferenceNumber) throws Exception {
        log.info("Prescription service - get approved prescription details");
        final Prescription prescription = prescriptionService.findByReferenceNumber(prescriptionReferenceNumber);
        if (Objects.isNull(prescription)) {
            log.error("Invalid Prescription reference number : {}", prescriptionReferenceNumber);
            throw new Exception("Invalid prescription reference number");
        }
        final PrescriptionAccess prescriptionAccess = prescriptionAccessRepository.findByAccessReferenceNumber(accessReferenceNumber);

        return prescriptionService.map(prescriptionAccess.getPrescription());
    }

    private PrescriptionAccessResponse map(final PrescriptionAccess prescriptionAccess) {
        return PrescriptionAccessResponse.builder()
                .doctorReferenceNumber(Objects.isNull(prescriptionAccess.getDoctor()) ? null : prescriptionAccess.getDoctor().getDoctorReferenceNumber())
                .patientReferenceNumber(prescriptionAccess.getPatient().getPatientReferenceNumber())
                .pharmacyReferenceNumber(Objects.isNull(prescriptionAccess.getPharmacist()) ? null : prescriptionAccess.getPharmacist().getPharmacyReferenceNumber())
                .prescriptionReferenceNumber(prescriptionAccess.getAccessReferenceNumber())
                .approved(prescriptionAccess.isApproved())
                .build();
    }
}
