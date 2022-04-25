package com.prescription.management.dto.request;

import com.prescription.management.constant.Gender;
import com.prescription.management.constant.UserType;
import lombok.Data;

@Data
public class RegistrationRequest {

    private String firstName;

    private String lastName;

    private Gender gender;

    private String email;

    private String contact;

    private String password;

    private UserType userType;
}
