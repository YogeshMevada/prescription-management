package com.prescription.management.rest;

import com.prescription.management.dto.request.AddRoleRequest;
import com.prescription.management.dto.request.UpdateRoleRequest;
import com.prescription.management.dto.response.ApiResponse;
import com.prescription.management.dto.response.RoleResponse;
import com.prescription.management.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

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
    public ResponseEntity<RoleResponse> getRole(@PathVariable("roleId") @Positive(message = "RoleId must be greater than zero") final long roleId) throws Exception {
        return ResponseEntity.ok(roleService.getRole(roleId));
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping(value = "/role")
    public ResponseEntity<List<RoleResponse>> getRoles() throws Exception {
        return ResponseEntity.ok(roleService.getRoles());
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping(value = "/role", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> create(@RequestBody @Valid final AddRoleRequest addRoleRequest) throws Exception {
        return ResponseEntity.ok(roleService.create(addRoleRequest));
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping(value = "/role/{roleId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> update(@PathVariable("roleId") @Positive(message = "RoleId must be greater than zero") final long roleId,
                                              @RequestBody @Valid final UpdateRoleRequest updateRoleRequest) throws Exception {
        return ResponseEntity.ok(roleService.update(roleId, updateRoleRequest));
    }
}