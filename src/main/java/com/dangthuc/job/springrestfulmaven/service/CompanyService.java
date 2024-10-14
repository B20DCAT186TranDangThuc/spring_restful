package com.dangthuc.job.springrestfulmaven.service;

import com.dangthuc.job.springrestfulmaven.entity.Company;
import com.dangthuc.job.springrestfulmaven.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company handleCreateCompany(Company reqCompany) {
        return this.companyRepository.save(reqCompany);
    }

    public List<Company> getAllCompanies() {
        return this.companyRepository.findAll();
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
        this.companyRepository.deleteById(id);
    }
}
