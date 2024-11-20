package com.dangthuc.job.springrestfulmaven.controller;

import com.dangthuc.job.springrestfulmaven.dto.ResultPaginationDTO;
import com.dangthuc.job.springrestfulmaven.dto.response.ResResumeDTO;
import com.dangthuc.job.springrestfulmaven.entity.Job;
import com.dangthuc.job.springrestfulmaven.entity.Resume;
import com.dangthuc.job.springrestfulmaven.entity.User;
import com.dangthuc.job.springrestfulmaven.service.JobService;
import com.dangthuc.job.springrestfulmaven.service.ResumeService;
import com.dangthuc.job.springrestfulmaven.service.UserService;
import com.dangthuc.job.springrestfulmaven.util.annotation.ApiMessage;
import com.dangthuc.job.springrestfulmaven.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @PostMapping("/resumes")
    @ApiMessage("create a resume")
    public ResponseEntity<?> createResume(@Valid @RequestBody Resume resume) throws IdInvalidException {

        if (!this.resumeService.checkResumeExistByUserAndJob(resume)) {
            throw new IdInvalidException("user id/job id khong ton tai");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.resumeService.saveOrUpdateResume(resume));
    }

    @PutMapping("/resumes")
    @ApiMessage("update status resume")
    public ResponseEntity<?> updateResume(@Valid @RequestBody Resume resume) throws IdInvalidException {

        return ResponseEntity.ok(this.resumeService.saveOrUpdateResume(resume));
    }

    @DeleteMapping("/resumes/{id}")
    @ApiMessage("delete a resume")
    public ResponseEntity<Void> deleteResume(@PathVariable("id") Long id) throws IdInvalidException {
        if (!this.resumeService.isExistById(id)) {
            throw new IdInvalidException("Resume not found with id: " + id);
        }
        this.resumeService.delete(id);

        return ResponseEntity.ok(null);
    }

    @GetMapping("/resumes/{id}")
    @ApiMessage("fetch a resume")
    public ResponseEntity<ResResumeDTO> fetchResume(@PathVariable("id") Long id) throws IdInvalidException {

        ResResumeDTO resResumeDTO = this.resumeService.getById(id);
        if (resResumeDTO == null) {
            throw new IdInvalidException("Resume not found with id: " + id);
        }

        return ResponseEntity.ok(resResumeDTO);
    }

    @GetMapping("/resumes")
    @ApiMessage("fetch all resumes")
    public ResponseEntity<ResultPaginationDTO> fetchAllResume(@Filter Specification<Resume> spec,
                                                              Pageable pageable) {
        return ResponseEntity.ok(this.resumeService.handleGetResume(spec, pageable));
    }
}
