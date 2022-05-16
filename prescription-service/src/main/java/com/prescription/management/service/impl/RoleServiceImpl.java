package com.prescription.management.service.impl;

import com.prescription.commons.constant.ResponseStatus;
import com.prescription.commons.constant.Status;
import com.prescription.commons.constant.UserType;
import com.prescription.commons.dto.PageRequest;
import com.prescription.commons.dto.request.AddRoleRequest;
import com.prescription.commons.dto.request.UpdateRoleRequest;
import com.prescription.commons.dto.response.ApiResponse;
import com.prescription.commons.dto.response.PageResponse;
import com.prescription.commons.dto.response.RoleResponse;
import com.prescription.management.entities.Role;
import com.prescription.management.repository.RoleRepository;
import com.prescription.management.service.RoleService;
import com.prescription.management.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(final RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findRole(final UserType userType) {
        return roleRepository.findByName(userType.name());
    }

    @Override
    public ApiResponse<RoleResponse> getRole(final long roleId) throws Exception {
        log.info("Get role by id : {}", roleId);
        final Role role = roleRepository.findById(roleId).orElse(null);
        if (Objects.isNull(role)) {
            log.error("Role not found");
            final ApiResponse apiResponse = new ApiResponse();
            apiResponse.setMessage("Role not found");
            apiResponse.setStatus(ResponseStatus.ERROR);
            return apiResponse;
        }
        final ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(map(role));
        apiResponse.setStatus(ResponseStatus.SUCCESS);
        return apiResponse;
    }

    @Override
    public PageResponse<RoleResponse> getRoles(final PageRequest pageRequest) throws Exception {
        log.info("Get all roles");
        final Pageable pageable = CommonUtil.getPageableInfo(pageRequest);
        final Page<Role> roles = roleRepository.findAll(pageable);

        return CommonUtil.createPageResponse(roles, this::map);
    }

    @Override
    @Transactional
    public ApiResponse<RoleResponse> create(final AddRoleRequest addRoleRequest) throws Exception {
        log.info("Add role");
        final Role roleByName = roleRepository.findByName(addRoleRequest.getName());
        if (Objects.nonNull(roleByName)) {
            log.error("Role already present");
            throw new Exception("Role already present");
        }
        final Role role = new Role();
        role.setName(addRoleRequest.getName());
        role.setStatus(Status.ACTIVE);
        final Role savedRole = roleRepository.save(role);

        log.info("Role created successfully");
        final ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(map(savedRole));
        apiResponse.setMessage("Role created successfully");
        apiResponse.setStatus(ResponseStatus.SUCCESS);
        return apiResponse;
    }

    @Override
    @Transactional
    public ApiResponse<RoleResponse> update(final long roleId, final UpdateRoleRequest updateRoleRequest) throws Exception {
        final Role role = roleRepository.findById(roleId).orElse(null);
        if (Objects.isNull(role)) {
            log.error("Role not present");
            throw new Exception("Role not present");
        }
        if (role.getStatus().equals(updateRoleRequest.getStatus())) {
            final ApiResponse apiResponse = new ApiResponse();
            apiResponse.setMessage("Role status is already same");
            apiResponse.setStatus(ResponseStatus.ERROR);
            return apiResponse;
        }
        if (Objects.nonNull(updateRoleRequest.getStatus()))
            role.setStatus(updateRoleRequest.getStatus());
        final Role savedRole = roleRepository.save(role);

        log.info("Role updated successfully");
        final ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(map(savedRole));
        apiResponse.setMessage("Role updated successfully");
        apiResponse.setStatus(ResponseStatus.SUCCESS);
        return apiResponse;
    }

    private RoleResponse map(final Role role) {
        final RoleResponse roleResponse = new RoleResponse();
        roleResponse.setId(role.getId());
        roleResponse.setName(role.getName());
        roleResponse.setStatus(role.getStatus());
        return roleResponse;
    }
}
