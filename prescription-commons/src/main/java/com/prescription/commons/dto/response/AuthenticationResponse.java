package com.prescription.commons.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AuthenticationResponse {

    private String token;
}
