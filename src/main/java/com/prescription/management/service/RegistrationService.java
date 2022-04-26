package com.prescription.management.service;

import com.prescription.management.dto.request.RegistrationRequest;
import com.prescription.management.dto.response.ApiResponse;

public interface RegistrationService {
    ApiResponse register(RegistrationRequest registrationRequest) throws Exception;
}
