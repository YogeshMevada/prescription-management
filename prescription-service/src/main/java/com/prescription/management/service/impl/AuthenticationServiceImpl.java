package com.prescription.management.service.impl;

import com.prescription.commons.dto.request.AuthenticationRequest;
import com.prescription.commons.dto.response.AuthenticationResponse;
import com.prescription.management.service.AuthenticationService;
import com.prescription.management.util.JwtUtil;
import com.prescription.management.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final SecurityUtil securityUtil;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthenticationServiceImpl(final AuthenticationManager authenticationManager,
                                     final UserDetailsService userDetailsService,
                                     final SecurityUtil securityUtil,
                                     final JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.securityUtil = securityUtil;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public AuthenticationResponse authenticate(final AuthenticationRequest authenticationRequest) throws Exception {
        validateRequest(authenticationRequest);

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (final BadCredentialsException e) {
            throw new Exception("Incorrect Username or Password", e);
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtUtil.generateToken(userDetails);
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    private void validateRequest(final AuthenticationRequest authenticationRequest) {
        decryptData(authenticationRequest);
    }

    private void decryptData(final AuthenticationRequest authenticationRequest) {
        final String aesDecryptedKey = securityUtil.getRsaDecryptedData(authenticationRequest.getAesEncryptedKey());
        authenticationRequest.setUsername(securityUtil.getAesDecryptedData(authenticationRequest.getUsername(), aesDecryptedKey));
        authenticationRequest.setPassword(securityUtil.getAesDecryptedData(authenticationRequest.getPassword(), aesDecryptedKey));
    }

}
