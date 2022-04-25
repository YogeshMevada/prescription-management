package com.prescription.management.service;

import com.prescription.management.constant.UserType;
import com.prescription.management.entities.Users;

public interface UserService {
    Users findUserByName(String username);

    Users findUserByEmail(String email);

    Users findUserByContact(String contact);

    void registerNewUser(Users users, UserType userType);
}
