package com.prescription.management.service.impl;

import com.prescription.management.constant.UserType;
import com.prescription.management.entities.*;
import com.prescription.management.repository.UserRepository;
import com.prescription.management.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final PharmacistService pharmacistService;

    @Autowired
    public UserServiceImpl(final UserRepository userRepository,
                           final RoleService roleService,
                           final DoctorService doctorService,
                           final PatientService patientService,
                           final PharmacistService pharmacistService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.pharmacistService = pharmacistService;
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
}
