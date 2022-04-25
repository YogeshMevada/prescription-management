package com.prescription.management.service;

import com.prescription.management.constant.UserType;
import com.prescription.management.dto.request.AddRoleRequest;
import com.prescription.management.dto.request.UpdateRoleRequest;
import com.prescription.management.dto.response.ApiResponse;
import com.prescription.management.dto.response.RoleResponse;
import com.prescription.management.entities.Role;

import java.util.List;

public interface RoleService {
    Role findRole(UserType userType);

    RoleResponse getRole(long roleId) throws Exception;

    List<RoleResponse> getRoles() throws Exception;

    ApiResponse create(AddRoleRequest addRoleRequest) throws Exception;

    ApiResponse update(long roleId, UpdateRoleRequest updateRoleRequest) throws Exception;
}
