package com.dangthuc.job.springrestfulmaven.service;

import com.dangthuc.job.springrestfulmaven.dto.ResultPaginationDTO;
import com.dangthuc.job.springrestfulmaven.entity.Permission;
import com.dangthuc.job.springrestfulmaven.entity.Role;
import com.dangthuc.job.springrestfulmaven.repository.PermissionRepository;
import com.dangthuc.job.springrestfulmaven.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    public boolean exitsByName(String name) {
        return roleRepository.existsByName(name);
    }

    public Role handleSave(Role role) {
        // check permission
        List<Long> idPermission = role.getPermissions().stream().map(Permission::getId).toList();
        List<Permission> permissions = this.permissionRepository.findAllById(idPermission);

        return this.roleRepository.findById(role.getId())
                .map(existingRole -> {
                    existingRole.setName(role.getName());
                    existingRole.setDescription(role.getDescription());
                    existingRole.setActive(role.isActive());
                    existingRole.setPermissions(permissions);
                    return this.roleRepository.save(existingRole);
                })
                // Nếu Role chưa tồn tại, tạo mới
                .orElseGet(() -> {
                    role.setPermissions(permissions);
                    return this.roleRepository.save(role);
                });
    }

    public Role fetchById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    public ResultPaginationDTO handleGetAllRole(Specification<Role> spec, Pageable pageable) {
        return PaginationService.handlePagination(spec, pageable, roleRepository);
    }

    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }
}
