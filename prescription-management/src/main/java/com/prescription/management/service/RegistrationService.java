package com.prescription.management.service;

import com.prescription.commons.dto.request.RegistrationRequest;
import com.prescription.commons.dto.response.ApiResponse;

public interface RegistrationService {
    ApiResponse register(RegistrationRequest registrationRequest) throws Exception;
}
