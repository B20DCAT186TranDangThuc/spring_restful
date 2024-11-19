package com.dangthuc.job.springrestfulmaven.service;

import com.dangthuc.job.springrestfulmaven.dto.response.ResCreateUserDTO;
import com.dangthuc.job.springrestfulmaven.dto.response.ResUpdateUserDTO;
import com.dangthuc.job.springrestfulmaven.dto.response.ResUserDTO;
import com.dangthuc.job.springrestfulmaven.dto.ResultPaginationDTO;
import com.dangthuc.job.springrestfulmaven.entity.Company;
import com.dangthuc.job.springrestfulmaven.entity.User;
import com.dangthuc.job.springrestfulmaven.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyService companyService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CompanyService companyService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.companyService = companyService;
    }

    public boolean isEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }

    public ResCreateUserDTO createUser(User user) {

        // check company
        if (user.getCompany() != null) {
            Optional<Company> companyOptional = this.companyService.findById(user.getCompany().getId());
            user.setCompany(companyOptional.orElse(null));
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return this.convertResCreateUserDTO(userRepository.save(user));
    }

    private ResCreateUserDTO convertResCreateUserDTO(User user) {
        ResCreateUserDTO.CompanyUser companyUser = new ResCreateUserDTO.CompanyUser();
        if (user.getCompany() != null) {
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
        }
        return ResCreateUserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .address(user.getAddress())
                .gender(user.getGender())
                .companyUser(companyUser)
                .createdAt(user.getCreatedAt())
                .build();
    }

    private ResUserDTO convertResUserDTO(User user) {

        ResUserDTO.CompanyUser companyUser = new ResUserDTO.CompanyUser();

        if (user.getCompany() != null) {
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
        }

        return ResUserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .address(user.getAddress())
                .gender(user.getGender())
                .companyUser(companyUser)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt()).build();
    }

    private ResUpdateUserDTO convertToUpdateUserDTO(User user) {

        ResUpdateUserDTO.CompanyUser companyUser = new ResUpdateUserDTO.CompanyUser();
        if (user.getCompany() != null) {
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
        }

        return ResUpdateUserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .age(user.getAge())
                .updatedAt(user.getUpdatedAt())
                .gender(user.getGender())
                .address(user.getAddress())
                .companyUser(companyUser)
                .build();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public ResultPaginationDTO fetchAllUser(Specification<User> spec, Pageable pageable) {
        Page<User> users = userRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(users.getTotalPages());
        meta.setTotal(users.getTotalElements());

        rs.setMeta(meta);

        List<ResUserDTO> listUser = users.getContent()
                .stream().map(item -> this.convertResUserDTO(item)).collect(Collectors.toList());

        rs.setResult(listUser);

        return rs;
    }

    public ResUserDTO fetchUserById(Long id) {
        Optional<User> user = this.userRepository.findById(id);
        if (user.isPresent()) {
            return this.convertResUserDTO(user.get());
        }
        return null;
    }

    public ResUpdateUserDTO updateUser(User request) {
        Optional<User> oUser = this.userRepository.findById(request.getId());
        User user = oUser.get();
        if (oUser.isPresent()) {
            user.setAddress(request.getAddress());
            user.setGender(request.getGender());
            user.setAge(request.getAge());
            user.setName(request.getName());

            // check company
            if (request.getCompany() != null) {
                Optional<Company> companyOptional = this.companyService.findById(request.getCompany().getId());
                user.setCompany(companyOptional.orElse(null));
            }
            user = userRepository.save(user);
        }

        return this.convertToUpdateUserDTO(user);
    }

    public User fetchUserByEmail(String username) {
        return userRepository.findByEmail(username);
    }

    public boolean isIdExist(Long id) {
        return this.userRepository.existsById(id);
    }

    public void updateUserToken(String token, String email) {
        User currentUser = this.fetchUserByEmail(email);

        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }
}
