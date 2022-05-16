package com.prescription.ui.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prescription.commons.dto.request.AuthenticationRequest;
import com.prescription.commons.dto.response.ApiResponse;
import com.prescription.commons.dto.response.AuthenticationResponse;
import com.prescription.commons.exception.ApiException;
import com.prescription.ui.service.PrescriptionClientService;
import com.prescription.ui.util.UrlHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

@Slf4j
@Service
public class PrescriptionClientServiceImpl implements PrescriptionClientService {

    private final Client client;

    private final UrlHelper urlHelper;

    private final ObjectMapper objectMapper;

    @Autowired
    public PrescriptionClientServiceImpl(final Client client, final UrlHelper urlHelper, final ObjectMapper objectMapper) {
        this.client = client;
        this.urlHelper = urlHelper;
        this.objectMapper = objectMapper;
    }

    @Override
    public ApiResponse<AuthenticationResponse> getUserDetails(final AuthenticationRequest userRequest, final HttpServletRequest request) {
        try {
            final Response response = client.target(urlHelper.GET_USER_URL)
                    .request()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.AUTHORIZATION, request.getParameter(HttpHeaders.AUTHORIZATION))
                    .post(Entity.entity(userRequest, MediaType.APPLICATION_JSON_VALUE));
            if (response.getStatus() != HttpStatus.OK.value()) {
                log.error("User details not found");
                throw new ApiException("Used details not found");
            }
            final String responseString = response.readEntity(String.class);
            return objectMapper.readValue(responseString, new TypeReference<ApiResponse<AuthenticationResponse>>() {
            });
        } catch (final Exception e) {
            log.error("Exception occurred while fetching user details", e);
            throw new ApiException(e.getMessage());
        }
    }
}
