package com.prescription.management.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class AddRoleRequest {

    @NotBlank(message = "Role name is mandatory")
    private String name;

    @NotEmpty(message = "Privileges for a role is mandatory")
    private List<String> privileges;

    public List<String> getPrivileges() {
        if (Objects.isNull(privileges)) {
            return new ArrayList<>();
        }
        return privileges;
    }
}
