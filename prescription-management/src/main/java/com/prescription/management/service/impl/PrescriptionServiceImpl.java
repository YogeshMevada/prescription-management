package com.prescription.management.service.impl;

import com.prescription.commons.constant.ResponseStatus;
import com.prescription.commons.dto.PageRequest;
import com.prescription.commons.dto.request.AddPrescriptionRequest;
import com.prescription.commons.dto.request.MedicineRequest;
import com.prescription.commons.dto.response.ApiResponse;
import com.prescription.commons.dto.response.MedicineResponse;
import com.prescription.commons.dto.response.PageResponse;
import com.prescription.commons.dto.response.PrescriptionResponse;
import com.prescription.management.entities.*;
import com.prescription.management.repository.PrescriptionRepository;
import com.prescription.management.service.DoctorService;
import com.prescription.management.service.MedicineService;
import com.prescription.management.service.PatientService;
import com.prescription.management.service.PrescriptionService;
import com.prescription.management.util.CommonUtil;
import com.prescription.management.validation.PrescriptionValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PrescriptionValidation prescriptionValidation;
    private final DoctorService doctorService;
    private final PatientService patientService;

    private final MedicineService medicineService;

    @Autowired
    public PrescriptionServiceImpl(final PrescriptionRepository prescriptionRepository,
                                   final PrescriptionValidation prescriptionValidation,
                                   final DoctorService doctorService,
                                   final PatientService patientService,
                                   final MedicineService medicineService) {
        this.prescriptionRepository = prescriptionRepository;
        this.prescriptionValidation = prescriptionValidation;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.medicineService = medicineService;
    }

    @Override
    public ApiResponse<PrescriptionResponse> getPrescription(final String prescriptionReferenceNumber) {
        log.info("Prescription service - get prescription by prescription reference number");
        final Prescription prescription = prescriptionRepository.findByPrescriptionReferenceNumber(prescriptionReferenceNumber);
        if (Objects.isNull(prescription)) {
            return ApiResponse.<PrescriptionResponse>builder()
                    .message(String.format("Prescription for reference-%s number not found", prescriptionReferenceNumber))
                    .status(ResponseStatus.ERROR)
                    .build();
        }
        return ApiResponse.<PrescriptionResponse>builder()
                .data(map(prescription))
                .status(ResponseStatus.SUCCESS)
                .build();
    }

    @Override
    public PageResponse<PrescriptionResponse> getPrescriptions(final PageRequest pageRequest) throws Exception {
        log.info("Prescription service - get all prescriptions");

        final Pageable pageable = CommonUtil.getPageableInfo(pageRequest);
        final Page<Prescription> prescriptions = prescriptionRepository.findAll(pageable);

        log.info("Prescriptions found:{}", prescriptions.getSize());
        return CommonUtil.createPageResponse(prescriptions, this::map);
    }

    @Override
    public Prescription findByReferenceNumber(final String prescriptionReferenceNumber) {
        log.info("Prescription service - get prescriptions by reference number");
        return prescriptionRepository.findByPrescriptionReferenceNumber(prescriptionReferenceNumber);
    }

    @Override
    public Prescription validateByReferenceNumber(String prescriptionReferenceNumber) throws Exception {
        final Prescription prescription = validateByReferenceNumber(prescriptionReferenceNumber);
        if (Objects.isNull(prescription)) {
            log.error("Invalid Prescription reference number : {}", prescriptionReferenceNumber);
            throw new Exception("Invalid prescription reference number");
        }
        return prescription;
    }

    @Override
    public PageResponse<PrescriptionResponse> getPrescriptionsOfPatient(final String patientReferenceNumber, final PageRequest pageRequest) throws Exception {
        log.info("Prescription service - get prescriptions of patient");
        patientService.validateByReferenceNumber(patientReferenceNumber);
        final Pageable pageable = CommonUtil.getPageableInfo(pageRequest);
        final Page<Prescription> prescriptions = prescriptionRepository.findByPatientReferenceNumber(patientReferenceNumber, pageable);

        log.info("Prescriptions found:{}", prescriptions.getSize());
        return CommonUtil.createPageResponse(prescriptions, this::map);
    }

    @Override
    public PageResponse<PrescriptionResponse> getPrescriptionsByDoctor(final String doctorReferenceNumber, final PageRequest pageRequest) throws Exception {
        log.info("Prescription service - get prescriptions by doctor");
        final Doctor doctor = doctorService.findByReferenceNumber(doctorReferenceNumber);
        if (Objects.isNull(doctor)) {
            log.error("Invalid Doctor reference number : {}", doctorReferenceNumber);
            throw new Exception("Invalid Doctor reference number");
        }

        final Pageable pageable = CommonUtil.getPageableInfo(pageRequest);
        final Page<Prescription> prescriptions = prescriptionRepository.findByDoctorReferenceNumber(doctorReferenceNumber, pageable);

        log.info("Prescriptions found:{}", prescriptions.getSize());
        return CommonUtil.createPageResponse(prescriptions, this::map);
    }

    @Override
    public ApiResponse<PrescriptionResponse> create(final String doctorReferenceNumber, final String patientReferenceNumber, final AddPrescriptionRequest addPrescriptionRequest) throws Exception {
        log.info("Prescription service - create");

        final Doctor doctor = doctorService.findByReferenceNumber(doctorReferenceNumber);
        if (Objects.isNull(doctor)) {
            log.error("Invalid Doctor reference number : {}", doctorReferenceNumber);
            throw new Exception("Invalid Doctor reference number");
        }
        final Patient patient = patientService.validateByReferenceNumber(patientReferenceNumber);
        final List<PrescriptionItem> prescriptionItems = new ArrayList<>();
        final List<MedicineRequest> invalidMedicines = addPrescriptionRequest.getMedicines().stream()
                .filter(medicineRequest -> {
                    final Medicine medicine = medicineService.findMedicine(medicineRequest.getMedicineId(), medicineRequest.getMedicineReferenceNumber());
                    if (Objects.nonNull(medicine)) {
                        prescriptionItems.add(map(medicineRequest, medicine));
                        return true;
                    }
                    return false;
                }).collect(Collectors.toList());

        if (!invalidMedicines.isEmpty() || prescriptionItems.size() != addPrescriptionRequest.getMedicines().size()) {
            log.error("Invalid Medicines found in prescription");
            throw new Exception("Invalid Medicines found in prescription");
        }

        final Prescription prescription = new Prescription();
        prescription.setAppointmentNumber(addPrescriptionRequest.getAppointmentNumber());
        prescription.setPrescriptionReferenceNumber(UUID.randomUUID().toString());
        prescription.setDoctor(doctor);
        prescription.setPatient(patient);
        prescription.getPrescriptionItems().addAll(prescriptionItems);
        final Prescription savedPrescription = prescriptionRepository.save(prescription);

        log.info("Prescription created successfully");
        return ApiResponse.<PrescriptionResponse>builder()
                .data(map(savedPrescription))
                .message("Prescription created successfully")
                .status(ResponseStatus.SUCCESS)
                .build();
    }

    @Override
    public PrescriptionResponse map(final Prescription prescription) {
        return PrescriptionResponse.builder()
                .appointmentNumber(prescription.getAppointmentNumber())
                .referenceNumber(prescription.getPrescriptionReferenceNumber())
                .doctorReferenceNumber(prescription.getDoctor().getDoctorReferenceNumber())
                .patientReferenceNumber(prescription.getPatient().getPatientReferenceNumber())
                .medicines(prescription.getPrescriptionItems().stream()
                        .map(prescriptionItem -> MedicineResponse.builder()
                                .medicineId(prescriptionItem.getMedicine().getId())
                                .brandName(prescriptionItem.getMedicine().getBrandName())
                                .activeIngredientName(prescriptionItem.getMedicine().getActiveIngredientName())
                                .medicineReferenceNumber(prescriptionItem.getMedicine().getMedicineReferenceNumber())
                                .quantity(prescriptionItem.getQuantity())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    private PrescriptionItem map(final MedicineRequest medicineRequest, final Medicine medicine) {
        final PrescriptionItem prescriptionItem = new PrescriptionItem();
        prescriptionItem.setMedicine(medicine);
        prescriptionItem.setQuantity(medicineRequest.getQuantity());
        return prescriptionItem;
    }

    private PrescriptionResponse map(final Prescription prescription, final String doctorReferenceNumber) {
        if (doctorReferenceNumber.equals(prescription.getDoctor().getDoctorReferenceNumber())) {
            return map(prescription);
        }
        return PrescriptionResponse.builder()
                .appointmentNumber(prescription.getAppointmentNumber())
                .referenceNumber(prescription.getPrescriptionReferenceNumber())
                .doctorReferenceNumber(prescription.getDoctor().getDoctorReferenceNumber())
                .patientReferenceNumber(prescription.getPatient().getPatientReferenceNumber())
                .build();
    }
}
