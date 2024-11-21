package com.dangthuc.job.springrestfulmaven.service;

import com.dangthuc.job.springrestfulmaven.dto.ResultPaginationDTO;
import com.dangthuc.job.springrestfulmaven.entity.Job;
import com.dangthuc.job.springrestfulmaven.entity.Permission;
import com.dangthuc.job.springrestfulmaven.entity.Role;
import com.dangthuc.job.springrestfulmaven.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    public boolean existPermission(Permission permission) {
        return this.permissionRepository.existsByApiPathAndMethodAndModule(
                permission.getApiPath(),
                permission.getMethod(),
                permission.getModule()
        );
    }

    public Permission handleSave(Permission permission) {
        return this.permissionRepository.findById(permission.getId())
                .map(curPermission -> {
                    curPermission.setModule(permission.getModule());
                    curPermission.setApiPath(permission.getApiPath());
                    curPermission.setMethod(permission.getMethod());
                    return this.permissionRepository.save(curPermission);
                })
                .orElseGet(() -> this.permissionRepository.save(permission));
    }

    public Permission fetchById(long id) {
        return this.permissionRepository.findById(id).orElse(null);
    }

    public ResultPaginationDTO handleGetPermission(Specification<Permission> spec, Pageable pageable) {
        return PaginationService.handlePagination(spec, pageable, permissionRepository);
    }

    public void deleteByid(Long id) {
        Optional<Permission> permissionOptional = this.permissionRepository.findById(id);
        Permission currentPermission = permissionOptional.get();
        currentPermission.getRoles().forEach(role -> role.getPermissions().remove(currentPermission));
        // delete permission
        this.permissionRepository.delete(currentPermission);
    }
}
