package com.prescription.commons.dto.response;

import com.prescription.commons.constant.ErrorCode;
import com.prescription.commons.constant.ResponseStatus;
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
