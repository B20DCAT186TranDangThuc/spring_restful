package com.dangthuc.job.springrestfulmaven.controller;

import com.dangthuc.job.springrestfulmaven.entity.User;
import com.dangthuc.job.springrestfulmaven.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/create")
    public ResponseEntity<User> create() {

        User user = new User();
        user.setName("Dang Thuc");
        user.setEmail("thuc@gmail.com");
        user.setPassword("Dang Thuc");

        return ResponseEntity.ok(userService.createUser(user));
    }
}
