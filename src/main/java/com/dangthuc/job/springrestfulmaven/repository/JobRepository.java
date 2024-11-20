package com.dangthuc.job.springrestfulmaven.repository;

import com.dangthuc.job.springrestfulmaven.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    Page<Job> findAll(Specification<Job> spec, Pageable pageable);

}
