package com.dangthuc.job.springrestfulmaven.service;

import com.dangthuc.job.springrestfulmaven.entity.User;
import com.dangthuc.job.springrestfulmaven.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public List<User> fetchAllUser() {
        return userRepository.findAll();
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
}
