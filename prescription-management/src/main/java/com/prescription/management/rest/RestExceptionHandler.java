package com.prescription.management.rest;

import com.prescription.commons.dto.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice(annotations = RestController.class)
public class RestExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleException(final Exception e) {
        log.error(e.getMessage(), e);
        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e.getLocalizedMessage());
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleException(final MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        final List<String> errors = e.getBindingResult().getAllErrors().stream()
                .map(objectError -> ((FieldError) objectError).getField().concat("-").concat(objectError.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), errors);
    }
}
