package com.prescription.management.service;

import com.prescription.commons.constant.UserType;
import com.prescription.commons.dto.request.AuthenticationRequest;
import com.prescription.commons.dto.response.ApiResponse;
import com.prescription.commons.dto.response.AuthenticationResponse;
import com.prescription.management.entities.Users;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    Users findUserByName(String username);

    Users findUserByEmail(String email);

    Users findUserByContact(String contact);

    void registerNewUser(Users users, UserType userType) throws Exception;

    ApiResponse<AuthenticationResponse> getUserDetails(AuthenticationRequest authenticationRequest, HttpServletRequest httpServletRequest);

    void decryptUserRequest(AuthenticationRequest authenticationRequest);
}
