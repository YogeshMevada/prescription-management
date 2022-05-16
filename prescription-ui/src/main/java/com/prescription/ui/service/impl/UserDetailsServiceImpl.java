package com.prescription.ui.service.impl;

import com.prescription.commons.dto.request.AuthenticationRequest;
import com.prescription.commons.dto.response.ApiResponse;
import com.prescription.commons.dto.response.AuthenticationResponse;
import com.prescription.commons.util.SecurityUtil;
import com.prescription.ui.service.PrescriptionClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PrescriptionClientService prescriptionClientService;
    private final SecurityUtil securityUtil;
    private final HttpServletRequest request;

    @Autowired
    public UserDetailsServiceImpl(final PrescriptionClientService prescriptionClientService,
                                  final SecurityUtil securityUtil,
                                  final HttpServletRequest request) {
        this.prescriptionClientService = prescriptionClientService;
        this.securityUtil = securityUtil;
        this.request = request;
    }

    private static SimpleGrantedAuthority apply(String role) {
        return new SimpleGrantedAuthority(role.toUpperCase());
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        log.info("Inside User Details Service Impl");
        final HttpSession session = request.getSession();
        final AuthenticationRequest authenticationRequest = getUserRequest(request);
        final ApiResponse<AuthenticationResponse> userDetailsResponse = prescriptionClientService.getUserDetails(authenticationRequest, request);
        final AuthenticationResponse authenticationResponse = userDetailsResponse.getData();

        session.setAttribute(HttpHeaders.AUTHORIZATION, authenticationResponse.getToken());

        return new User(authenticationResponse.getUsername(), authenticationRequest.getPassword(), getAuthorities(authenticationResponse.getRoles()));
    }

    private AuthenticationRequest getUserRequest(final HttpServletRequest request) {
        final AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setAesEncryptedKey(request.getParameter("aesEncryptedKey"));
        authenticationRequest.setUsername(request.getParameter("loginUsername"));
        authenticationRequest.setPassword(request.getParameter("loginPassword"));
        return authenticationRequest;
    }

    public List<GrantedAuthority> getAuthorities(final List<String> roles) {
        return roles.stream()
                .map(UserDetailsServiceImpl::apply)
                .collect(Collectors.toList());
    }
}
