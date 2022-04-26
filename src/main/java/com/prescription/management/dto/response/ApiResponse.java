package com.prescription.management.dto.response;

import com.prescription.management.constant.ErrorCode;
import com.prescription.management.constant.ResponseStatus;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApiResponse<T> {

    private T data;

    private String message;

    private ResponseStatus status;

    private ErrorCode errorCode;
}
