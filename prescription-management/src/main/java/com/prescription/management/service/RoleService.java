package com.prescription.management.service;

import com.prescription.commons.constant.UserType;
import com.prescription.commons.dto.PageRequest;
import com.prescription.commons.dto.request.AddRoleRequest;
import com.prescription.commons.dto.request.UpdateRoleRequest;
import com.prescription.commons.dto.response.ApiResponse;
import com.prescription.commons.dto.response.PageResponse;
import com.prescription.commons.dto.response.RoleResponse;
import com.prescription.management.entities.Role;

public interface RoleService {
    Role findRole(UserType userType);

    ApiResponse<RoleResponse> getRole(long roleId) throws Exception;

    PageResponse<RoleResponse> getRoles(PageRequest pageRequest) throws Exception;

    ApiResponse<RoleResponse> create(AddRoleRequest addRoleRequest) throws Exception;

    ApiResponse<RoleResponse> update(long roleId, UpdateRoleRequest updateRoleRequest) throws Exception;
}
