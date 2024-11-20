package com.dangthuc.job.springrestfulmaven.service;

import com.dangthuc.job.springrestfulmaven.dto.ResultPaginationDTO;
import com.dangthuc.job.springrestfulmaven.entity.Skill;
import com.dangthuc.job.springrestfulmaven.repository.SkillRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public boolean isNameExist(String name) {
        return this.skillRepository.existsByName(name);
    }

    public Skill createSkill(Skill skill) {
        return this.skillRepository.save(skill);
    }

    public Skill fetchSkillById(long id) {
        return this.skillRepository.findById(id).orElse(null);
    }

    public Skill updateSkill(Skill currentSkill) {
        return this.skillRepository.save(currentSkill);
    }

    public ResultPaginationDTO handleGetSkill(Specification<Skill> spec, Pageable pageable) {
        Page<Skill> pSkill = this.skillRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pSkill.getTotalPages());
        meta.setTotal(pSkill.getTotalElements());

        rs.setMeta(meta);
        rs.setResult(pSkill.getContent());

        return rs;
    }

    public void deleteById(Long id) {
        // delete job (inside job_skill table)
        Optional<Skill> skillOptional = this.skillRepository.findById(id);
        Skill currentSkill = skillOptional.get();
        currentSkill.getJobs().forEach(job -> job.getSkills().remove(currentSkill));
        // delete skill
        this.skillRepository.delete(currentSkill);
    }
}
