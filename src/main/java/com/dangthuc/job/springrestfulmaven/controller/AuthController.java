package com.dangthuc.job.springrestfulmaven.controller;

import com.dangthuc.job.springrestfulmaven.dto.LoginDTO;
import com.dangthuc.job.springrestfulmaven.dto.ResLoginDTO;
import com.dangthuc.job.springrestfulmaven.entity.User;
import com.dangthuc.job.springrestfulmaven.service.UserService;
import com.dangthuc.job.springrestfulmaven.util.SecurityUtil;
import com.dangthuc.job.springrestfulmaven.util.annotation.ApiMessage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;

    @Value("${dangthuc.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder,
                          SecurityUtil securityUtil,
                          UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(), loginDTO.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User currentUser = this.userService.fetchUserByEmail(loginDTO.getUsername());

        ResLoginDTO res = ResLoginDTO.builder()
                .user(ResLoginDTO.UserLogin.builder()
                        .id(currentUser.getId())
                        .email(currentUser.getEmail())
                        .name(currentUser.getName())
                        .build())
                .build();
        // create a tokent
        String accessToken = this.securityUtil.createAccessToken(authentication, res.getUser());

        res.setAccessToken(accessToken);

        String refresh_token = this.securityUtil.createRefreshToken(loginDTO.getUsername(), res);

        // update user
        this.userService.updateUserToken(refresh_token, loginDTO.getUsername());

        // set cookies
        ResponseCookie resCookies = ResponseCookie
                .from("refresh_token", refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(res);
    }

    @GetMapping("/auth/account")
    @ApiMessage("fetch account")
    public ResponseEntity<ResLoginDTO.UserLogin> getAccount() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ?
                SecurityUtil.getCurrentUserLogin().get() : "";

        User currentUser = this.userService.fetchUserByEmail(email);

        ResLoginDTO.UserLogin res = ResLoginDTO.UserLogin.builder()
                .id(currentUser.getId())
                .email(currentUser.getEmail())
                .name(currentUser.getName())
                .build();

        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/auth/refresh")
    @ApiMessage("Get user by refresh token")
    public ResponseEntity<String> getRefreshToken(
            @CookieValue(name = "refresh_token") String refresh_token
    ) {

        // check valid
        Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refresh_token);
        String email = decodedToken.getSubject();
        return ResponseEntity.ok().body(email);
    }

}
