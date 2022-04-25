package com.prescription.management.service;

import com.prescription.management.dto.request.AuthenticationRequest;
import com.prescription.management.dto.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) throws Exception;
}
