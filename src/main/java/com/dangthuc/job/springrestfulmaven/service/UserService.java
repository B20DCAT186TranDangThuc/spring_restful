package com.dangthuc.job.springrestfulmaven.service;

import com.dangthuc.job.springrestfulmaven.dto.ResultPaginationDTO;
import com.dangthuc.job.springrestfulmaven.entity.Meta;
import com.dangthuc.job.springrestfulmaven.entity.User;
import com.dangthuc.job.springrestfulmaven.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public ResultPaginationDTO fetchAllUser(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta meta = new Meta();

        meta.setPage(users.getNumber() + 1);
        meta.setTotal(users.getTotalElements());
        meta.setPages(users.getTotalPages());
        meta.setTotal(users.getTotalElements());

        rs.setMeta(meta);
        rs.setResult(users.getContent());

        return rs;
    }

    public User fetchUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User updateUser(User request) {
        User user = this.fetchUserById(request.getId());
        if (user != null) {
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());
            user = userRepository.save(user);
        }

        return user;
    }

    public User fetchUserByEmail(String username) {
        return userRepository.findByEmail(username);
    }
}
