package com.dangthuc.job.springrestfulmaven.controller;

import com.dangthuc.job.springrestfulmaven.dto.ResultPaginationDTO;
import com.dangthuc.job.springrestfulmaven.entity.Permission;
import com.dangthuc.job.springrestfulmaven.entity.Role;
import com.dangthuc.job.springrestfulmaven.service.RoleService;
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
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/roles")
    @ApiMessage("create a role")
    public ResponseEntity<Role> createRole(@RequestBody Role role) throws IdInvalidException {
        // check name
        if (roleService.exitsByName(role.getName())) {
            throw new IdInvalidException("Role voi name = " + role.getName() + " da ton tai");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.handleSave(role));
    }

    @PutMapping("/roles")
    @ApiMessage("update a role")
    public ResponseEntity<Role> updateRole(@RequestBody Role role) throws IdInvalidException {

        if (roleService.fetchById(role.getId()) == null) {
            throw new IdInvalidException("Role voi id = " + role.getId() + "khong ton tai");
        }

//        if (roleService.exitsByName(role.getName())) {
//            throw new IdInvalidException("Role voi name = " + role.getName() + " da ton tai");
//        }

        return ResponseEntity.status(HttpStatus.OK).body(roleService.handleSave(role));
    }

    @GetMapping("/roles/{id}")
    @ApiMessage("fetch a role")
    public ResponseEntity<Role> fetchRole(@PathVariable("id") Long id) throws IdInvalidException {
        Role role = roleService.fetchById(id);
        if (role == null) {
            throw new IdInvalidException("khong tim thay role voi id = " + id);
        }
        return ResponseEntity.status(HttpStatus.OK).body(role);

    }

    @GetMapping("/roles")
    @ApiMessage("fetch all role")
    public ResponseEntity<ResultPaginationDTO> fetchAllRole(@Filter Specification<Role> spec, Pageable pageable) {
        return ResponseEntity.ok(roleService.handleGetAllRole(spec, pageable));
    }

    @DeleteMapping("/roles/{id}")
    @ApiMessage("delete a role")
    public ResponseEntity<Void> deleteRole(@PathVariable("id") Long id) throws IdInvalidException {
        if (roleService.fetchById(id) == null) {
            throw new IdInvalidException("khong tim thay role voi id = " + id);
        }

        roleService.deleteById(id);

        return ResponseEntity.ok(null);
    }
}
