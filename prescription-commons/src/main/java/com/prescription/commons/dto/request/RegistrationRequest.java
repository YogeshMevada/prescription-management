package com.prescription.commons.dto.request;

import com.prescription.commons.constant.Gender;
import com.prescription.commons.constant.UserType;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class RegistrationRequest {

    @NotBlank(message = "User's first name is mandatory")
    private String firstName;

    @NotBlank(message = "User's last name is mandatory")
    private String lastName;

    private Gender gender;

    @NotBlank(message = "User's email is mandatory")
    @Email(message = "Email is not valid", regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    private String email;

    @NotBlank(message = "User's contact number is mandatory")
    @Pattern(regexp = "(^$|\\d{10})")
    private String contact;

    @NotBlank(message = "User's password is mandatory")
    private String password;

    @NotBlank(message = "User type is mandatory")
    private UserType userType;
}
