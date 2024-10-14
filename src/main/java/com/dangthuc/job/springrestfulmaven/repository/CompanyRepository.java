package com.dangthuc.job.springrestfulmaven.repository;

import com.dangthuc.job.springrestfulmaven.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
}
