package com.prescription.management.dto.request;

import com.prescription.management.constant.Status;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class UpdateRoleRequest {

    @NotBlank(message = "Role name is mandatory")
    private String name;

    private List<String> privileges;

    private Status status;

    public List<String> getPrivileges() {
        if (Objects.isNull(privileges)) {
            return new ArrayList<>();
        }
        return privileges;
    }
}
