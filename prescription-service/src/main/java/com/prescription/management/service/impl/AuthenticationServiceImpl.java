package com.prescription.management.service.impl;

import com.prescription.commons.dto.request.AuthenticationRequest;
import com.prescription.commons.dto.response.AuthenticationResponse;
import com.prescription.commons.util.SecurityUtil;
import com.prescription.management.service.AuthenticationService;
import com.prescription.management.service.UserService;
import com.prescription.management.util.JwtUtil;
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

    private final UserService userService;

    private final JwtUtil jwtUtil;

    @Autowired
    public AuthenticationServiceImpl(final AuthenticationManager authenticationManager,
                                     final UserDetailsService userDetailsService,
                                     final SecurityUtil securityUtil,
                                     final UserService userService,
                                     final JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public AuthenticationResponse authenticate(final AuthenticationRequest authenticationRequest) throws Exception {
        userService.decryptUserRequest(authenticationRequest);

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (final BadCredentialsException e) {
            throw new Exception("Incorrect Username or Password", e);
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtUtil.generateToken(userDetails);
        final AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setUsername(authenticationRequest.getUsername());
        authenticationResponse.setToken(token);
        return authenticationResponse;
    }
}
