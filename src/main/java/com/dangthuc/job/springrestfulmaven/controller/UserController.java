package com.dangthuc.job.springrestfulmaven.controller;

import com.dangthuc.job.springrestfulmaven.dto.ResCreateUserDTO;
import com.dangthuc.job.springrestfulmaven.dto.ResUpdateUserDTO;
import com.dangthuc.job.springrestfulmaven.dto.ResUserDTO;
import com.dangthuc.job.springrestfulmaven.dto.ResultPaginationDTO;
import com.dangthuc.job.springrestfulmaven.entity.User;
import com.dangthuc.job.springrestfulmaven.service.UserService;
import com.dangthuc.job.springrestfulmaven.util.annotation.ApiMessage;
import com.dangthuc.job.springrestfulmaven.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("users")
    @ApiMessage("create one users")
    public ResponseEntity<ResCreateUserDTO> create(@Valid @RequestBody User request) throws IdInvalidException {

        if (this.userService.isEmailExist(request.getEmail())) {
            throw new IdInvalidException("Email " + request.getEmail() + " already exists");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @DeleteMapping("users/{id}")
    @ApiMessage("delete one user")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws IdInvalidException {

        if (!this.userService.isIdExist(id)) {
            throw new IdInvalidException("Id " + id + " not found");
        }

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("users")
    @ApiMessage("fetch all users")
    public ResponseEntity<ResultPaginationDTO> getAll(@Filter Specification<User> spec, Pageable pageable) {

        return ResponseEntity.ok(userService.fetchAllUser(spec, pageable));
    }

    @GetMapping("users/{id}")
    @ApiMessage("fetch one user")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable("id") Long id) throws IdInvalidException {

        if (!this.userService.isIdExist(id)) {
            throw new IdInvalidException("Id " + id + " not found");
        }

        return ResponseEntity.ok(userService.fetchUserById(id));
    }

    @PutMapping("users")
    @ApiMessage("update one user")
    public ResponseEntity<ResUpdateUserDTO> update(@RequestBody User request) throws IdInvalidException {
        if (!this.userService.isIdExist(request.getId())) {
            throw new IdInvalidException("User với id = " + request.getId() + " không tồn tại");
        }
        return ResponseEntity.ok(userService.updateUser(request));
    }
}
