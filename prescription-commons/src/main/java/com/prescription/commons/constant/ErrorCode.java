package com.prescription.commons.constant;

public enum ErrorCode {
    // User validation error code
    VU001("VU001", "Invalid user id"),

    // Medicine validation error code
    VM001("VM001", "Invalid medicine"),

    // Prescription access validation error code
    PA001("PA001", "Prescription does not belong to patient");

    final String code;
    final String message;

    ErrorCode(final String code, final String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
