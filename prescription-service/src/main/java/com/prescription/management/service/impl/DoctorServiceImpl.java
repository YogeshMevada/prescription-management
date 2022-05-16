package com.prescription.management.service.impl;

import com.prescription.commons.constant.ResponseStatus;
import com.prescription.commons.dto.request.UpdateDoctorRequest;
import com.prescription.commons.dto.response.ApiResponse;
import com.prescription.commons.dto.response.DoctorResponse;
import com.prescription.commons.dto.response.PageResponse;
import com.prescription.commons.dto.search.DoctorSearchRequest;
import com.prescription.management.entities.Doctor;
import com.prescription.management.repository.DoctorRepository;
import com.prescription.management.service.DoctorService;
import com.prescription.management.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    @Autowired
    public DoctorServiceImpl(final DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public Doctor findByReferenceNumber(final String doctorReferenceNumber) {
        log.info("Doctor service - find By ReferenceNumber");
        return doctorRepository.findByDoctorReferenceNumber(doctorReferenceNumber);
    }

    @Override
    public ApiResponse<DoctorResponse> getDoctor(final String doctorReferenceNumber) {
        log.info("Doctor service - find all doctors");
        final Doctor doctor = findByReferenceNumber(doctorReferenceNumber);
        if (Objects.isNull(doctor)) {
            log.error("Doctor not found for reference number-{}", doctorReferenceNumber);
            final ApiResponse apiResponse = new ApiResponse();
            apiResponse.setMessage(String.format("Doctor not found for reference number-%s", doctorReferenceNumber));
            apiResponse.setStatus(ResponseStatus.ERROR);
            return apiResponse;
        }
        final ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(map(doctor));
        apiResponse.setStatus(ResponseStatus.SUCCESS);
        return apiResponse;
    }

    @Override
    public PageResponse<DoctorResponse> getDoctors(final DoctorSearchRequest doctorSearchRequest) {
        log.info("Doctor service - find all doctors");
        final Pageable pageable = CommonUtil.getPageableInfo(doctorSearchRequest);
        final Page<Doctor> doctors = doctorRepository.findAll(searchSpecification(doctorSearchRequest), pageable);

        return CommonUtil.createPageResponse(doctors, this::map);
    }

    @Override
    public Doctor save(final Doctor doctor) {

        return doctorRepository.save(doctor);
    }

    @Override
    public ApiResponse<DoctorResponse> update(final String doctorReferenceNumber, final UpdateDoctorRequest updateDoctorRequest) {
        log.info("Doctor service - update");
        final Doctor doctor = findByReferenceNumber(doctorReferenceNumber);
        if (Objects.isNull(doctor)) {
            log.error("Doctor not found by reference number-{}", doctorReferenceNumber);
            final ApiResponse apiResponse = new ApiResponse();
            apiResponse.setMessage(String.format("Doctor not found by reference number-%s", doctorReferenceNumber));
            apiResponse.setStatus(ResponseStatus.ERROR);
            return apiResponse;
        }
        doctor.setDegree(doctor.getDegree());
        final Doctor savedDoctor = doctorRepository.save(doctor);

        log.info("Doctor updated successfully");
        final ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(map(savedDoctor));
        apiResponse.setMessage("Doctor updated successfully");
        apiResponse.setStatus(ResponseStatus.SUCCESS);
        return apiResponse;
    }

    private DoctorResponse map(final Doctor doctor) {
        final DoctorResponse doctorResponse = new DoctorResponse();
        doctorResponse.setDoctorReferenceNumber(doctor.getDoctorReferenceNumber());
        doctorResponse.setFirstName(doctor.getUser().getFirstName());
        doctorResponse.setLastName(doctor.getUser().getLastName());
        doctorResponse.setDegree(doctor.getDegree());
        return doctorResponse;
    }

    private Specification<Doctor> searchSpecification(final DoctorSearchRequest doctorSearchRequest) {
        Specification<Doctor> specification = (doctor, query, cb) -> cb.equal(cb.literal(1), 1);
        if (StringUtils.isNotEmpty(doctorSearchRequest.getDoctorReferenceNumber())) {
            specification = specification.and((doctor, query, cb) -> cb.equal(doctor.get("doctorReferenceNumber"), doctorSearchRequest.getDoctorReferenceNumber()));
        }
        if (StringUtils.isNotEmpty(doctorSearchRequest.getFirstName())) {
            specification = specification.and((doctor, query, cb) -> cb.like(doctor.get("user").get("firstName"), "%".concat(doctorSearchRequest.getFirstName()).concat("%")));
        }
        if (StringUtils.isNotEmpty(doctorSearchRequest.getLastName())) {
            specification = specification.and((doctor, query, cb) -> cb.like(doctor.get("user").get("lastName"), "%".concat(doctorSearchRequest.getLastName()).concat("%")));
        }
        if (StringUtils.isNotEmpty(doctorSearchRequest.getDegree())) {
            specification = specification.and((doctor, query, cb) -> cb.like(doctor.get("degree"), "%".concat(doctorSearchRequest.getDegree()).concat("%")));
        }
        return specification;
    }
}
