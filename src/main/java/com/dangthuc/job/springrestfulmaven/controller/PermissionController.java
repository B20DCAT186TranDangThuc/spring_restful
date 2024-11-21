package com.dangthuc.job.springrestfulmaven.controller;

import com.dangthuc.job.springrestfulmaven.dto.ResultPaginationDTO;
import com.dangthuc.job.springrestfulmaven.entity.Permission;
import com.dangthuc.job.springrestfulmaven.service.PermissionService;
import com.dangthuc.job.springrestfulmaven.util.annotation.ApiMessage;
import com.dangthuc.job.springrestfulmaven.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @PostMapping("/permissions")
    @ApiMessage("Create a permission")
    public ResponseEntity<Permission> createPermission(@RequestBody Permission permission) throws IdInvalidException {

        if (this.permissionService.existPermission(permission)) {
            throw new IdInvalidException("Permission already exist");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.permissionService.handleSave(permission));
    }

    @PutMapping("/permissions")
    @ApiMessage("Update a permisson")
    public ResponseEntity<Permission> updatePermission(@RequestBody Permission permission) throws IdInvalidException {

        if (this.permissionService.fetchById(permission.getId()) == null) {
            throw new IdInvalidException("Permission does not exist with id = " + permission.getId());
        }

        if (this.permissionService.existPermission(permission)) {
            throw new IdInvalidException("Permission already exist");
        }
        return ResponseEntity.ok(this.permissionService.handleSave(permission));
    }

    @GetMapping("/permissions/{id}")
    @ApiMessage("Fetch a permission")
    public ResponseEntity<Permission> fetchPermission(@PathVariable("id") Long id) throws IdInvalidException {

        Permission permission = this.permissionService.fetchById(id);
        if (permission == null) {
            throw new IdInvalidException("Permission not found");
        }
        return ResponseEntity.ok(permission);
    }

    @GetMapping("/permissions")
    @ApiMessage("Fetch all permission")
    public ResponseEntity<ResultPaginationDTO> fetchAllPermission(@Filter Specification<Permission> spec, Pageable pageable) {

        return ResponseEntity.ok(this.permissionService.handleGetPermission(spec, pageable));
    }

    @DeleteMapping("/permissions/{id}")
    @ApiMessage("Delete a permission")
    public ResponseEntity<Void> deletePermission(@PathVariable("id") Long id) throws IdInvalidException {
        if (this.permissionService.fetchById(id) == null) {
            throw new IdInvalidException("Permission does not exist with id = " + id);
        }

        this.permissionService.deleteByid(id);
        return ResponseEntity.ok(null);
    }
}

