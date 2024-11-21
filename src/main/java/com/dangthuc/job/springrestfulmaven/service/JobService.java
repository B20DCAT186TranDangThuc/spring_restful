package com.dangthuc.job.springrestfulmaven.service;

import com.dangthuc.job.springrestfulmaven.dto.ResultPaginationDTO;
import com.dangthuc.job.springrestfulmaven.dto.response.ResJobDTO;
import com.dangthuc.job.springrestfulmaven.entity.Job;
import com.dangthuc.job.springrestfulmaven.entity.Skill;
import com.dangthuc.job.springrestfulmaven.repository.JobRepository;
import com.dangthuc.job.springrestfulmaven.repository.SkillRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private SkillRepository skillRepository;

    public ResJobDTO handleSaveJob(Job job) {
        // kiem tra skill
        if (job.getSkills() != null) {
            List<Long> skillIds = job.getSkills()
                    .stream().map(Skill::getId)
                    .toList();

            job.setSkills(this.skillRepository.findByIdIn(skillIds));
        }
        return this.convertToResJobDTO(this.jobRepository.save(job));
    }

    public ResJobDTO convertToResJobDTO(Job job) {
        return ResJobDTO.builder()
                .id(job.getId())
                .name(job.getName())
                .location(job.getLocation())
                .salary(job.getSalary())
                .quantity(job.getQuantity())
                .level(job.getLevel())
                .startDate(job.getStartDate())
                .endDate(job.getEndDate())
                .isActive(job.isActive())
                .skills(job.getSkills().stream().map(Skill::getName).collect(Collectors.toList()))
                .createdAt(job.getCreatedAt())
                .createdBy(job.getCreatedBy())
                .updatedAt(job.getUpdatedAt())
                .updatedBy(job.getUpdatedBy())
                .build();
    }

    public Optional<Job> fetchJobById(long id) {
        return this.jobRepository.findById(id);
    }

    public ResultPaginationDTO handleGetJob(Specification<Job> spec, Pageable pageable) {
        Page<Job> pageUser = this.jobRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);

        rs.setResult(pageUser.getContent());

        return rs;
    }

    public void delete(long id) {
        this.jobRepository.deleteById(id);
    }

    public boolean isIdExits(long id) {
        return this.jobRepository.existsById(id);
    }
}
