package com.dangthuc.job.springrestfulmaven.service;

import com.dangthuc.job.springrestfulmaven.dto.ResultPaginationDTO;
import com.dangthuc.job.springrestfulmaven.dto.response.ResCreateResumeDTO;
import com.dangthuc.job.springrestfulmaven.dto.response.ResResumeDTO;
import com.dangthuc.job.springrestfulmaven.entity.Job;
import com.dangthuc.job.springrestfulmaven.entity.Resume;
import com.dangthuc.job.springrestfulmaven.entity.User;
import com.dangthuc.job.springrestfulmaven.repository.JobRepository;
import com.dangthuc.job.springrestfulmaven.repository.ResumeRepository;
import com.dangthuc.job.springrestfulmaven.repository.UserRepository;
import com.dangthuc.job.springrestfulmaven.util.error.IdInvalidException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ResumeService {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;

    public ResCreateResumeDTO saveOrUpdateResume(@Valid Resume resume) throws IdInvalidException {
        Resume savedResume;

        if (resume.getId() != null) {
            // Kiểm tra xem đối tượng có tồn tại trong cơ sở dữ liệu không
            Resume existingResume = this.resumeRepository.findById(resume.getId())
                    .orElseThrow(() -> new IdInvalidException("Resume not found with id: " + resume.getId()));

            existingResume.setStatus(resume.getStatus());
            savedResume = this.resumeRepository.save(existingResume);
        } else {
            // Tạo mới đối tượng
            savedResume = this.resumeRepository.save(resume);
        }

        // Chuẩn bị đối tượng DTO để trả về
        ResCreateResumeDTO resumeDTO = new ResCreateResumeDTO();
        resumeDTO.setId(savedResume.getId());
        resumeDTO.setCreatedAt(savedResume.getCreatedAt());
        resumeDTO.setCreatedBy(savedResume.getCreatedBy());
        resumeDTO.setUpdatedAt(savedResume.getUpdatedAt());
        resumeDTO.setUpdatedBy(savedResume.getUpdatedBy());
        return resumeDTO;
    }


    public boolean checkResumeExistByUserAndJob(Resume resume) {
        if (resume.getUser() == null || resume.getJob() == null) {
            return false;
        }
        User user = this.userRepository.findById(resume.getUser().getId()).orElse(null);
        Job job = this.jobRepository.findById(resume.getJob().getId()).orElse(null);
        return user != null && job != null;
    }

    public boolean isExistById(Long id) {
        return this.resumeRepository.existsById(id);
    }

    public void delete(Long id) {
        this.resumeRepository.deleteById(id);
    }

    public ResResumeDTO getById(Long id) {
        return toResResumeDTO(this.resumeRepository.findById(id).orElse(null));
    }

    public ResResumeDTO toResResumeDTO(Resume resume) {
        if (resume == null) {
            return null;
        }

        return ResResumeDTO.builder()
                .id(resume.getId())
                .email(resume.getEmail())
                .url(resume.getUrl())
                .status(resume.getStatus())
                .createdAt(resume.getCreatedAt())
                .updatedAt(resume.getUpdatedAt())
                .createdBy(resume.getCreatedBy())
                .updatedBy(resume.getUpdatedBy())
                .user(resume.getUser() != null
                        ? new ResResumeDTO.UserResume(
                        resume.getUser().getId(),
                        resume.getUser().getName()
                )
                        : null)
                .job(resume.getJob() != null
                        ? new ResResumeDTO.JobResume(
                        resume.getJob().getId(),
                        resume.getJob().getName()
                )
                        : null)
                .build();
    }

    public ResultPaginationDTO handleGetResume(Specification<Resume> spec, Pageable pageable) {
        return PaginationService.handlePagination(spec, pageable, resumeRepository);
    }

}
