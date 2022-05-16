package com.prescription.management.rest;

import com.prescription.commons.dto.request.AuthenticationRequest;
import com.prescription.commons.dto.response.ApiResponse;
import com.prescription.commons.dto.response.AuthenticationResponse;
import com.prescription.management.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequestMapping("api/v1")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @Secured({"ROLE_ADMIN", "ROLE_DOCTOR", "ROLE_PATIENT", "ROLE_PHARMACIST"})
    @PostMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<AuthenticationResponse>> getUserDetails(@RequestBody @Valid final AuthenticationRequest authenticationRequest,
                                                                              HttpServletRequest httpServletRequest) {
        log.info("Get user details request");
        return ResponseEntity.ok(userService.getUserDetails(authenticationRequest, httpServletRequest));
    }
}
