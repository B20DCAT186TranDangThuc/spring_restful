package com.dangthuc.job.springrestfulmaven.repository;

import com.dangthuc.job.springrestfulmaven.entity.Skill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    boolean existsByName(String name);

    Page<Skill> findAll(Specification<Skill> spec, Pageable pageable);

    List<Skill> findByIdIn(List<Long> skillIds);
}
