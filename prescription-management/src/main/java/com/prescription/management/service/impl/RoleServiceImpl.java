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
            return ApiResponse.<RoleResponse>builder()
                    .message("Role not found")
                    .status(ResponseStatus.ERROR)
                    .build();
        }
        return ApiResponse.<RoleResponse>builder()
                .data(map(role))
                .status(ResponseStatus.SUCCESS)
                .build();
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
        return ApiResponse.<RoleResponse>builder()
                .data(map(savedRole))
                .message("Role created successfully")
                .status(ResponseStatus.SUCCESS)
                .build();
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
            return ApiResponse.<RoleResponse>builder()
                    .message("Role status is already same")
                    .status(ResponseStatus.ERROR)
                    .build();
        }
        if (Objects.nonNull(updateRoleRequest.getStatus()))
            role.setStatus(updateRoleRequest.getStatus());
        final Role savedRole = roleRepository.save(role);

        log.info("Role updated successfully");
        return ApiResponse.<RoleResponse>builder()
                .data(map(savedRole))
                .message("Role updated successfully")
                .status(ResponseStatus.SUCCESS)
                .build();
    }

    private RoleResponse map(final Role role) {
        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .status(role.getStatus())
                .build();
    }
}
