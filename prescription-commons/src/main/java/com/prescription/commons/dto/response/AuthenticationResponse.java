package com.prescription.commons.dto.response;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@Data
public class AuthenticationResponse {

    private String username;

    private String password;

    private List<String> roles;

    private String token;
}
