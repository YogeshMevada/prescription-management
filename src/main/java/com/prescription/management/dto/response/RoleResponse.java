package com.prescription.management.dto.response;

import com.prescription.management.constant.Status;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RoleResponse {

    private long id;

    private String name;

    private Status status;
}
