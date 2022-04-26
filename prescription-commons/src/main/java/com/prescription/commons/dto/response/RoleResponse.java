package com.prescription.commons.dto.response;

import com.prescription.commons.constant.Status;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RoleResponse {

    private long id;

    private String name;

    private Status status;
}
