package com.prescription.management.service.impl;

import com.prescription.management.constant.ResponseStatus;
import com.prescription.management.dto.request.AddPrescriptionRequest;
import com.prescription.management.dto.request.MedicineRequest;
import com.prescription.management.dto.response.ApiResponse;
import com.prescription.management.dto.response.MedicineResponse;
import com.prescription.management.dto.response.PrescriptionResponse;
import com.prescription.management.entities.*;
import com.prescription.management.repository.PrescriptionRepository;
import com.prescription.management.service.DoctorService;
import com.prescription.management.service.MedicineService;
import com.prescription.management.service.PatientService;
import com.prescription.management.service.PrescriptionService;
import com.prescription.management.validation.PrescriptionValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Prescription findByReferenceNumber(final String prescriptionReferenceNumber) {
        log.info("Prescription service - get prescriptions by reference number");
        return prescriptionRepository.findByPrescriptionReferenceNumber(prescriptionReferenceNumber);
    }

    @Override
    public List<PrescriptionResponse> getPrescriptionsOfPatient(final String patientReferenceNumber) throws Exception {
        log.info("Prescription service - get prescriptions of patient");
        final Patient patient = patientService.findByReferenceNumber(patientReferenceNumber);
        if (Objects.isNull(patient)) {
            log.error("Invalid Patient reference number : {}", patientReferenceNumber);
            throw new Exception("Invalid patient reference number");
        }
        final List<Prescription> prescriptions = prescriptionRepository.findByPatientReferenceNumber(patientReferenceNumber);
        if (prescriptions.isEmpty()) {
            log.error("No prescriptions found for patient: {}", patientReferenceNumber);
            throw new Exception("No prescriptions found for patient");
        }
        return prescriptions.stream()
                .map(prescription -> map(prescription))
                .collect(Collectors.toList());
    }

    @Override
    public List<PrescriptionResponse> getPrescriptionsByDoctor(final String doctorReferenceNumber) throws Exception {
        log.info("Prescription service - get prescriptions by doctor");
        final Doctor doctor = doctorService.findByReferenceNumber(doctorReferenceNumber);
        if (Objects.isNull(doctor)) {
            log.error("Invalid Doctor reference number : {}", doctorReferenceNumber);
            throw new Exception("Invalid Doctor reference number");
        }
        final List<Prescription> prescriptions = prescriptionRepository.findByDoctorReferenceNumber(doctorReferenceNumber);
        if (prescriptions.isEmpty()) {
            log.error("No prescriptions found for doctor: {}", doctorReferenceNumber);
            throw new Exception("No prescriptions found for doctor");
        }
        return prescriptions.stream()
                .map(prescription -> map(prescription))
                .collect(Collectors.toList());
    }

    @Override
    public List<PrescriptionResponse> getPrescriptionsOfPatientForDoctor(final String doctorReferenceNumber, final String patientReferenceNumber) throws Exception {
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
        final List<Prescription> prescriptions = prescriptionRepository.findByPatientReferenceNumber(patientReferenceNumber);
        if (prescriptions.isEmpty()) {
            log.error("No prescriptions found for patient: {}", patientReferenceNumber);
            throw new Exception("No prescriptions found for patient");
        }
        return prescriptions.stream()
                .map(prescription -> map(prescription, doctorReferenceNumber))
                .collect(Collectors.toList());
    }

    @Override
    public ApiResponse create(final String doctorReferenceNumber, final String patientReferenceNumber, final AddPrescriptionRequest addPrescriptionRequest) throws Exception {
        log.info("Prescription service - create");

        prescriptionValidation.validate(addPrescriptionRequest);

        final Doctor doctor = doctorService.findByReferenceNumber(doctorReferenceNumber);
        if (Objects.isNull(doctor)) {
            log.error("Invalid Doctor reference number : {}", doctorReferenceNumber);
            throw new Exception("Invalid Doctor reference number");
        }
        final Patient patient = patientService.findByReferenceNumber(patientReferenceNumber);
        if (Objects.isNull(patient)) {
            log.error("Invalid Patient reference number : {}", patientReferenceNumber);
            throw new Exception("Invalid Patient reference number");
        }
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

        return ApiResponse.builder()
                .message("Prescription created successfully")
                .status(ResponseStatus.SUCCESS)
                .build();
    }

    private PrescriptionItem map(final MedicineRequest medicineRequest, final Medicine medicine) {
        final PrescriptionItem prescriptionItem = new PrescriptionItem();
        prescriptionItem.setMedicine(medicine);
        prescriptionItem.setQuantity(medicineRequest.getQuantity());
        return prescriptionItem;
    }

    private PrescriptionResponse map(final Prescription prescription) {
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
