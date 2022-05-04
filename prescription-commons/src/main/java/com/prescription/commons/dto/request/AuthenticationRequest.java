package com.prescription.commons.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AuthenticationRequest {

    @NotBlank(message = "Username is mandatory!")
    private String username;

    @NotBlank(message = "Password is mandatory!")
    private String password;

    @NotBlank(message = "Encrypted key is mandatory!")
    private String aesEncryptedKey;
}
