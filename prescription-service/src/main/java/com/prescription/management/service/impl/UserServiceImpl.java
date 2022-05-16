package com.prescription.management.service.impl;

import com.prescription.commons.constant.ResponseStatus;
import com.prescription.commons.constant.Status;
import com.prescription.commons.constant.UserType;
import com.prescription.commons.dto.request.AuthenticationRequest;
import com.prescription.commons.dto.response.ApiResponse;
import com.prescription.commons.dto.response.AuthenticationResponse;
import com.prescription.commons.util.SecurityUtil;
import com.prescription.management.entities.*;
import com.prescription.management.repository.UserRepository;
import com.prescription.management.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final PharmacistService pharmacistService;
    private final SecurityUtil securityUtil;

    @Autowired
    public UserServiceImpl(final UserRepository userRepository,
                           final RoleService roleService,
                           final DoctorService doctorService,
                           final PatientService patientService,
                           final PharmacistService pharmacistService,
                           final SecurityUtil securityUtil) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.pharmacistService = pharmacistService;
        this.securityUtil = securityUtil;
    }

    @Override
    public Users findUserByName(final String username) {
        final Users users = userRepository.findByUsername(username);
        if (Objects.isNull(users)) {
            log.error("User not found");
            throw new UsernameNotFoundException("User not found");
        }
        return users;
    }

    @Override
    public Users findUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Users findUserByContact(final String contact) {
        return userRepository.findByContact(contact);
    }

    @Override
    public void registerNewUser(final Users users, final UserType userType) throws Exception {
        log.info("Inside Register new user");
        final Role role = roleService.findRole(userType);
        createUserType(users, userType);
        userRepository.save(users);
        log.info("User created successfully");
    }

    @Override
    public ApiResponse<AuthenticationResponse> getUserDetails(final AuthenticationRequest authenticationRequest, final HttpServletRequest httpServletRequest) {
        decryptUserRequest(authenticationRequest);
        final Users user = userRepository.findByUsername(authenticationRequest.getUsername());
        final AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setUsername(authenticationRequest.getUsername());
        authenticationResponse.setPassword(user.getPassword());
        authenticationResponse.setRoles(getRoles(user.getRoles()));
        authenticationResponse.setToken(httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION));
        final ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(authenticationResponse);
        apiResponse.setStatus(ResponseStatus.SUCCESS);
        return apiResponse;
    }

    @Override
    public void decryptUserRequest(final AuthenticationRequest authenticationRequest) {
        final String aesDecryptedKey = securityUtil.getRsaDecryptData(authenticationRequest.getAesEncryptedKey());
        authenticationRequest.setUsername(securityUtil.getAesDecryptData(authenticationRequest.getUsername(), aesDecryptedKey));
        authenticationRequest.setPassword(securityUtil.getAesDecryptData(authenticationRequest.getPassword(), aesDecryptedKey));
    }

    private void createUserType(final Users users, final UserType userType) throws Exception {
        switch (userType) {
            case DOCTOR:
                final Doctor doctor = new Doctor();
                doctor.setUser(users);
                doctor.setDoctorReferenceNumber(UUID.randomUUID().toString());
                final Doctor savedDoctor = doctorService.save(doctor);
                log.info("Doctor created successfully - {}", savedDoctor.getDoctorReferenceNumber());
                break;
            case PATIENT:
                final Patient patient = new Patient();
                patient.setUser(users);
                patient.setPatientReferenceNumber(UUID.randomUUID().toString());
                final Patient savedPatient = patientService.save(patient);
                log.info("Patient created successfully - {}", savedPatient.getPatientReferenceNumber());
                break;
            case PHARMACIST:
                final Pharmacist pharmacist = new Pharmacist();
                pharmacist.setUser(users);
                pharmacist.setPharmacyReferenceNumber(UUID.randomUUID().toString());
                final Pharmacist savedPharmacist = pharmacistService.save(pharmacist);
                log.info("Pharmacist created successfully - {}", savedPharmacist.getPharmacyReferenceNumber());
                break;
            default:
                throw new Exception("User type invalid");
        }
    }

    private List<String> getRoles(final Set<Role> roles) {
        return roles.stream()
                .filter(role -> Status.ACTIVE.equals(role.getStatus()))
                .map(role -> {
                    String name = role.getName();
                    if (!name.startsWith("ROLE_")) {
                        name = "ROLE_".concat(name);
                    }
                    return name.toUpperCase();
                })
                .collect(Collectors.toList());
    }
}
