package com.prescription.management.service.impl;

import com.prescription.management.constant.ErrorCode;
import com.prescription.management.constant.ResponseStatus;
import com.prescription.management.dto.PageRequest;
import com.prescription.management.dto.response.ApiResponse;
import com.prescription.management.dto.response.PageResponse;
import com.prescription.management.dto.response.PrescriptionAccessResponse;
import com.prescription.management.dto.response.PrescriptionResponse;
import com.prescription.management.entities.*;
import com.prescription.management.repository.PrescriptionAccessRepository;
import com.prescription.management.service.*;
import com.prescription.management.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

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
    public PageResponse<PrescriptionAccessResponse> getPrescriptionAccessRequestsForPatient(final String patientReferenceNumber, final PageRequest pageRequest) throws Exception {
        log.info("Prescription service - get prescription access requests for patient");
        patientService.validateByReferenceNumber(patientReferenceNumber);
        final Pageable pageable = CommonUtil.getPageableInfo(pageRequest);
        final Page<PrescriptionAccess> prescriptionAccesses = prescriptionAccessRepository.findByPatientReferenceNumber(patientReferenceNumber, false, pageable);

        return CommonUtil.createPageResponse(prescriptionAccesses, this::map);
    }

    @Override
    public PageResponse<PrescriptionAccessResponse> getPrescriptionAccessRequestsByDoctor(final String doctorReferenceNumber, final PageRequest pageRequest) throws Exception {
        log.info("Prescription service - get prescription access requests by doctor");
        final Doctor doctor = doctorService.findByReferenceNumber(doctorReferenceNumber);
        if (Objects.isNull(doctor)) {
            log.error("Invalid Doctor reference number : {}", doctorReferenceNumber);
            throw new Exception("Invalid Doctor reference number");
        }
        final Pageable pageable = CommonUtil.getPageableInfo(pageRequest);
        final Page<PrescriptionAccess> prescriptionAccesses = prescriptionAccessRepository.findByDoctorReferenceNumber(doctorReferenceNumber, false, pageable);

        return CommonUtil.createPageResponse(prescriptionAccesses, this::map);
    }

    @Override
    public PageResponse<PrescriptionAccessResponse> getPrescriptionAccessRequestsByPharmacist(final String pharmacyReferenceNumber, final PageRequest pageRequest) throws Exception {
        log.info("Prescription service - get prescription access requests by pharmacist");
        final Pharmacist pharmacist = pharmacistService.findByReferenceNumber(pharmacyReferenceNumber);
        if (Objects.isNull(pharmacist)) {
            log.error("Invalid Pharmacist reference number : {}", pharmacyReferenceNumber);
            throw new Exception("Invalid Pharmacist reference number");
        }
        final Pageable pageable = CommonUtil.getPageableInfo(pageRequest);
        final Page<PrescriptionAccess> prescriptionAccesses = prescriptionAccessRepository.findByPharmacyReferenceNumber(pharmacyReferenceNumber, false, pageable);

        return CommonUtil.createPageResponse(prescriptionAccesses, this::map);
    }

    @Override
    @Transactional
    public ApiResponse<PrescriptionAccessResponse> requestPrescriptionByDoctor(final String doctorReferenceNumber, final String patientReferenceNumber, final String prescriptionReferenceNumber) throws Exception {
        log.info("Prescription service - get prescriptions of patient for doctor");
        final Doctor doctor = doctorService.findByReferenceNumber(doctorReferenceNumber);
        if (Objects.isNull(doctor)) {
            log.error("Invalid Doctor reference number : {}", doctorReferenceNumber);
            throw new Exception("Invalid Doctor reference number");
        }
        final Patient patient = patientService.validateByReferenceNumber(patientReferenceNumber);
        final Prescription prescription = prescriptionService.findByReferenceNumber(prescriptionReferenceNumber);
        if (Objects.isNull(prescription)) {
            log.error("Invalid Prescription reference number : {}", prescriptionReferenceNumber);
            throw new Exception("Invalid prescription reference number");
        }
        if (doctorReferenceNumber.equals(prescription.getDoctor().getDoctorReferenceNumber())) {
            return ApiResponse.<PrescriptionAccessResponse>builder()
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
        final PrescriptionAccess savedPrescriptionAccess = prescriptionAccessRepository.save(prescriptionAccess);

        log.info("Prescription successfully requested from patient");
        return ApiResponse.<PrescriptionAccessResponse>builder()
                .data(map(savedPrescriptionAccess))
                .message("Prescription successfully requested from patient")
                .status(ResponseStatus.SUCCESS)
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<PrescriptionAccessResponse> requestPrescriptionByPharmacist(final String pharmacyReferenceNumber, final String patientReferenceNumber, final String prescriptionReferenceNumber) throws Exception {
        log.info("Prescription service - get prescriptions of patient for pharmacist");
        final Pharmacist pharmacist = pharmacistService.findByReferenceNumber(pharmacyReferenceNumber);
        if (Objects.isNull(pharmacist)) {
            log.error("Invalid Pharmacy reference number : {}", pharmacyReferenceNumber);
            throw new Exception("Invalid Pharmacy reference number");
        }
        final PrescriptionAccess prescriptionAccessFound = prescriptionAccessRepository.findByPharmacyReferenceNumberAndPrescriptionReferenceNumber(pharmacyReferenceNumber, prescriptionReferenceNumber, false);
        if (Objects.nonNull(prescriptionAccessFound)) {
            return ApiResponse.<PrescriptionAccessResponse>builder()
                    .message("You already have the access to the prescription")
                    .status(ResponseStatus.SUCCESS)
                    .build();
        }

        final Patient patient = patientService.validateByReferenceNumber(patientReferenceNumber);
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
        final PrescriptionAccess savePrescriptionAccess = prescriptionAccessRepository.save(prescriptionAccess);

        log.info("Prescription successfully requested from patient");
        return ApiResponse.<PrescriptionAccessResponse>builder()
                .data(map(prescriptionAccess))
                .message("Prescription successfully requested from patient")
                .status(ResponseStatus.SUCCESS)
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<PrescriptionAccessResponse> providePrescriptionAccess(final String patientReferenceNumber, final String accessReferenceNumber) throws Exception {
        log.info("Prescription service - provide prescription access");
        patientService.validateByReferenceNumber(patientReferenceNumber);
        final PrescriptionAccess prescriptionAccess = prescriptionAccessRepository.findByAccessReferenceNumber(accessReferenceNumber);
        if (Objects.isNull(prescriptionAccess)) {
            log.error("Invalid Prescription access reference number : {}", accessReferenceNumber);
            throw new Exception("Invalid Prescription access reference number");
        }
        if (patientReferenceNumber.equals(prescriptionAccess.getPatient().getPatientReferenceNumber())) {
            log.error("Invalid prescription-{} for Patient-{}", prescriptionAccess.getPrescription().getPrescriptionReferenceNumber(), accessReferenceNumber);
            return ApiResponse.<PrescriptionAccessResponse>builder()
                    .message("Invalid prescription for Patient")
                    .status(ResponseStatus.ERROR)
                    .errorCode(ErrorCode.PA001)
                    .build();
        }

        prescriptionAccess.setApproved(true);
        final PrescriptionAccess savedPrescriptionAccess = prescriptionAccessRepository.save(prescriptionAccess);

        log.info("Prescription access provided successfully");
        return ApiResponse.<PrescriptionAccessResponse>builder()
                .data(map(savedPrescriptionAccess))
                .message("Prescription access provided successfully")
                .status(ResponseStatus.SUCCESS)
                .build();
    }

    @Override
    public PrescriptionResponse getApprovedPrescription(final String prescriptionReferenceNumber, final String accessReferenceNumber) throws Exception {
        log.info("Prescription service - get approved prescription details");
        prescriptionService.validateByReferenceNumber(prescriptionReferenceNumber);
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
