package com.prescription.management.exception;

import org.springframework.security.core.AuthenticationException;

public class UserAlreadyExistAuthenticationException extends AuthenticationException {
    public UserAlreadyExistAuthenticationException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

    public UserAlreadyExistAuthenticationException(final String msg) {
        super(msg);
    }
}
