package com.prescription.management.rest;

import com.prescription.commons.dto.PageRequest;
import com.prescription.commons.dto.request.AddRoleRequest;
import com.prescription.commons.dto.request.UpdateRoleRequest;
import com.prescription.commons.dto.response.ApiResponse;
import com.prescription.commons.dto.response.PageResponse;
import com.prescription.commons.dto.response.RoleResponse;
import com.prescription.management.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Slf4j
@Validated
@RestController
@RequestMapping("api/v1")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(final RoleService roleService) {
        this.roleService = roleService;
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping(value = "/role/{roleId}")
    public ResponseEntity<ApiResponse<RoleResponse>> getRole(@PathVariable("roleId") final @Positive(message = "RoleId must be greater than zero") long roleId) throws Exception {
        return ResponseEntity.ok(roleService.getRole(roleId));
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping(value = "/role", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<RoleResponse>> getRoles(@RequestBody @NotNull(message = "Page request is mandatory") final PageRequest pageRequest) throws Exception {
        return ResponseEntity.ok(roleService.getRoles(pageRequest));
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping(value = "/role", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<RoleResponse>> create(@RequestBody @Valid final AddRoleRequest addRoleRequest) throws Exception {
        return ResponseEntity.ok(roleService.create(addRoleRequest));
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping(value = "/role/{roleId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> update(@PathVariable("roleId") @Positive(message = "RoleId must be greater than zero") final long roleId,
                                              @RequestBody @Valid final UpdateRoleRequest updateRoleRequest) throws Exception {
        return ResponseEntity.ok(roleService.update(roleId, updateRoleRequest));
    }
}