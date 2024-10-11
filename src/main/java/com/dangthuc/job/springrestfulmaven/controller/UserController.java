package com.dangthuc.job.springrestfulmaven.controller;

import com.dangthuc.job.springrestfulmaven.entity.User;
import com.dangthuc.job.springrestfulmaven.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public ResponseEntity<User> create(@RequestBody User request) {

        return ResponseEntity.ok(userService.createUser(request));
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {

        userService.deleteUser(id);

        return ResponseEntity.ok("Xoa thanh cong");
    }
}
