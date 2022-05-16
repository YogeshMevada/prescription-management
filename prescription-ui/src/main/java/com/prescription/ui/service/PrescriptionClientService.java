package com.prescription.ui.service;

import com.prescription.commons.dto.request.AuthenticationRequest;
import com.prescription.commons.dto.response.ApiResponse;
import com.prescription.commons.dto.response.AuthenticationResponse;

import javax.servlet.http.HttpServletRequest;

public interface PrescriptionClientService {
    ApiResponse<AuthenticationResponse> getUserDetails(AuthenticationRequest userRequest, HttpServletRequest request);
}
