package com.prescription.commons.exception;

import org.springframework.core.NestedRuntimeException;

public class ApiException extends NestedRuntimeException {

    public ApiException(final String message) {
        super(message);
    }

    public ApiException(final String message, Throwable cause) {
        super(message, cause);
    }
}
