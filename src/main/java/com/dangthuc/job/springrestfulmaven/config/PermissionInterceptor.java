package com.dangthuc.job.springrestfulmaven.config;

import com.dangthuc.job.springrestfulmaven.entity.Permission;
import com.dangthuc.job.springrestfulmaven.entity.Role;
import com.dangthuc.job.springrestfulmaven.entity.User;
import com.dangthuc.job.springrestfulmaven.service.UserService;
import com.dangthuc.job.springrestfulmaven.util.SecurityUtil;
import com.dangthuc.job.springrestfulmaven.util.error.IdInvalidException;
import com.dangthuc.job.springrestfulmaven.util.error.PermissionException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.List;

public class PermissionInterceptor implements HandlerInterceptor {

    @Autowired
    UserService userService;

    @Override
    @Transactional
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response, Object handler)
            throws Exception {

        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        System.out.println(">>> RUN preHandle");
        System.out.println(">>> path= " + path);
        System.out.println(">>> httpMethod= " + httpMethod);
        System.out.println(">>> requestURI= " + requestURI);

        String email = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        if (email != null && !email.isEmpty()) {
            User user = userService.fetchUserByEmail(email);
            if (user != null) {
                Role role = user.getRole();
                if (role != null) {
                    List<Permission> permissions = role.getPermissions();
                    boolean isAllow = permissions.stream().anyMatch(item ->
                            item.getApiPath().equals(path) && item.getMethod().equals(httpMethod)
                    );
                    if (!isAllow) {
                        throw new PermissionException("Ban khong co quyen truy cap trang nay");
                    }
                } else {
                    throw new PermissionException("Ban khong co quyen truy cap trang nay");
                }
            }
        }

        return true;
    }

}