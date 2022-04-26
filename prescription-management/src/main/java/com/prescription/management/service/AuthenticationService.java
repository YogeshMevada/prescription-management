package com.prescription.management.service;

import com.prescription.commons.dto.request.AuthenticationRequest;
import com.prescription.commons.dto.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) throws Exception;
}
