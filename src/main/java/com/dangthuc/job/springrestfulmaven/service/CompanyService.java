package com.dangthuc.job.springrestfulmaven.service;

import com.dangthuc.job.springrestfulmaven.dto.ResultPaginationDTO;
import com.dangthuc.job.springrestfulmaven.entity.Company;
import com.dangthuc.job.springrestfulmaven.entity.User;
import com.dangthuc.job.springrestfulmaven.repository.CompanyRepository;
import com.dangthuc.job.springrestfulmaven.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public CompanyService(CompanyRepository companyRepository,
                          UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    public Company handleCreateCompany(Company reqCompany) {
        return this.companyRepository.save(reqCompany);
    }

    public ResultPaginationDTO handleGetCompany(Specification<Company> spec, Pageable pageable) {
        Page<Company> pCompany = this.companyRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pCompany.getTotalPages());
        meta.setTotal(pCompany.getTotalElements());

        rs.setMeta(meta);
        rs.setResult(pCompany.getContent());

        return rs;
    }

    public Company handleUpdateCompany(Company reqCompany) {
        Optional<Company> company = this.companyRepository.findById(reqCompany.getId());
        if (company.isPresent()) {
            Company curCompany = company.get();
            curCompany.setName(reqCompany.getName());
            curCompany.setAddress(reqCompany.getAddress());
            curCompany.setDescription(reqCompany.getDescription());
            curCompany.setLogo(reqCompany.getLogo());

            return this.companyRepository.save(curCompany);
        }
        return null;
    }

    public void handleDeleteCompany(Long id) {
        Optional<Company> company = this.companyRepository.findById(id);
        if (company.isPresent()) {
            List<User> users = company.get().getUsers();
            this.userRepository.deleteAll(users);
        }
        this.companyRepository.deleteById(id);
    }

    public Optional<Company> findById(long id) {
        return companyRepository.findById(id);
    }
}
