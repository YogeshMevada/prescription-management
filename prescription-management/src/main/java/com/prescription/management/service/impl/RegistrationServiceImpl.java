package com.prescription.management.service.impl;

import com.prescription.commons.constant.ResponseStatus;
import com.prescription.commons.constant.Status;
import com.prescription.commons.dto.request.RegistrationRequest;
import com.prescription.commons.dto.response.ApiResponse;
import com.prescription.management.entities.Users;
import com.prescription.management.exception.UserAlreadyExistAuthenticationException;
import com.prescription.management.service.RegistrationService;
import com.prescription.management.service.UserService;
import com.prescription.management.validation.UserValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    private final UserValidation userValidation;

    @Autowired
    public RegistrationServiceImpl(final UserService userService,
                                   final PasswordEncoder passwordEncoder,
                                   final UserValidation userValidation) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userValidation = userValidation;
    }

    @Override
    public ApiResponse register(final RegistrationRequest registrationRequest) throws Exception {
        log.info("User registration");

        userValidation.validateUserInput(registrationRequest);

        final Users userByEmail = userService.findUserByEmail(registrationRequest.getEmail());
        if (Objects.nonNull(userByEmail)) {
            log.error("Email is already associated with existing user");
            throw new UserAlreadyExistAuthenticationException("Email is already associated with existing user");
        }

        final Users userByContact = userService.findUserByContact(registrationRequest.getContact());
        if (Objects.nonNull(userByContact)) {
            log.error("Contact number is already associated with existing user");
            throw new UserAlreadyExistAuthenticationException("Contact number is already associated with existing user");
        }

        final Users users = map(registrationRequest);
        userService.registerNewUser(users, registrationRequest.getUserType());

        log.info("User registration successful");
        return ApiResponse.builder()
                .message("User registration successful")
                .status(ResponseStatus.SUCCESS)
                .build();
    }

    private Users map(final RegistrationRequest registrationRequest) {
        final Users users = new Users();
        users.setFirstName(registrationRequest.getFirstName());
        users.setLastName(registrationRequest.getLastName());
        users.setGender(registrationRequest.getGender());
        users.setEmail(registrationRequest.getEmail());
        users.setContact(registrationRequest.getContact());
        users.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        users.setUserType(registrationRequest.getUserType());
        users.setStatus(Status.ACTIVE);
        return users;
    }
}
