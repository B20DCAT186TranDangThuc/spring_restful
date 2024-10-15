package com.dangthuc.job.springrestfulmaven.controller;

import com.dangthuc.job.springrestfulmaven.dto.ResultPaginationDTO;
import com.dangthuc.job.springrestfulmaven.entity.User;
import com.dangthuc.job.springrestfulmaven.service.UserService;
import com.dangthuc.job.springrestfulmaven.util.annotation.ApiMessage;
import com.dangthuc.job.springrestfulmaven.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("users")
    @ApiMessage("create one users")
    public ResponseEntity<User> create(@RequestBody User request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @DeleteMapping("users/{id}")
    @ApiMessage("delete one user")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws IdInvalidException {

        if (id >= 1500) {
            throw new IdInvalidException("Id khong lon hon 1500");
        }

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("users")
    @ApiMessage("fetch all users")
    public ResponseEntity<ResultPaginationDTO> getAll(
            @Filter Specification<User> spec, Pageable pageable) {

        return ResponseEntity.ok(userService.fetchAllUser(spec, pageable));
    }

    @GetMapping("users/{id}")
    @ApiMessage("fetch one user")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {

        return ResponseEntity.ok(userService.fetchUserById(id));
    }

    @PutMapping("users")
    @ApiMessage("update one user")
    public ResponseEntity<User> update(@RequestBody User request) {
        return ResponseEntity.ok(userService.updateUser(request));
    }
}
