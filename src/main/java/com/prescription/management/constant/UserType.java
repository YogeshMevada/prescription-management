package com.prescription.management.constant;

import org.apache.commons.lang3.StringUtils;

public enum UserType {

    ADMIN("ADMIN"),
    PATIENT("PATIENT"),
    DOCTOR("DOCTOR"),
    PHARMACIST("PHARMACIST");

    private final String role;

    private UserType(final String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return role;
    }

    public String getRole() {
        return role;
    }

    public static UserType getUserType(final String role) {
        if (StringUtils.isEmpty(role)) {
            throw new IllegalArgumentException();
        }
        for (final UserType userType : values()) {
            if (role.equalsIgnoreCase(userType.getRole())) {
                return userType;
            }
        }
        throw new IllegalArgumentException();
    }
}
