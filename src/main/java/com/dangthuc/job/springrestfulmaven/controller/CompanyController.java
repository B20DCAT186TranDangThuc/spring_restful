package com.dangthuc.job.springrestfulmaven.controller;

import com.dangthuc.job.springrestfulmaven.entity.Company;
import com.dangthuc.job.springrestfulmaven.entity.RestResponse;
import com.dangthuc.job.springrestfulmaven.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/companies")
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.status(HttpStatus.OK).body(this.companyService.getAllCompanies());
    }

    @PostMapping("/companies")
    public ResponseEntity<?> getCompany(@Valid @RequestBody Company reqCompany) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.companyService.handleCreateCompany(reqCompany));
    }

    @PutMapping("/companies")
    public ResponseEntity<?> updateCompany(@Valid @RequestBody Company reqCompany) {
        return ResponseEntity.ok(this.companyService.handleUpdateCompany(reqCompany));
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        this.companyService.handleDeleteCompany(id);
        return ResponseEntity.ok(null);
    }
}
