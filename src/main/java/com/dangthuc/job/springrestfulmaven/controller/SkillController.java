package com.dangthuc.job.springrestfulmaven.controller;

import com.dangthuc.job.springrestfulmaven.dto.ResultPaginationDTO;
import com.dangthuc.job.springrestfulmaven.entity.Company;
import com.dangthuc.job.springrestfulmaven.entity.Skill;
import com.dangthuc.job.springrestfulmaven.service.SkillService;
import com.dangthuc.job.springrestfulmaven.util.annotation.ApiMessage;
import com.dangthuc.job.springrestfulmaven.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    @ApiMessage("create a skill")
    public ResponseEntity<Skill> createSkill(@RequestBody Skill skill) throws IdInvalidException {

        // check name
        if (this.skillService.isNameExist(skill.getName())) {
            throw new IdInvalidException("Skill voi name " + skill.getName() + " da ton tai");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.skillService.createSkill(skill));
    }

    @PutMapping("/skills")
    @ApiMessage("update a skill")
    public ResponseEntity<Skill> updateSkill(@RequestBody Skill skill) throws IdInvalidException {

        Skill currentSkill = this.skillService.fetchSkillById(skill.getId());
        if (currentSkill == null) {
            throw new IdInvalidException("Skill voi id " + skill.getId() + " khong ton tai");
        }
        if (this.skillService.isNameExist(skill.getName())) {
            throw new IdInvalidException("Skill voi name " + skill.getName() + " da ton tai");
        }
        currentSkill.setName(skill.getName());
        return ResponseEntity.ok(this.skillService.updateSkill(currentSkill));
    }

    @GetMapping("/skills")
    @ApiMessage("fetch all skills")
    public ResponseEntity<ResultPaginationDTO> getAllCompanies(
            @Filter Specification<Skill> spec,
            Pageable pageable) {
        return ResponseEntity.ok(this.skillService.handleGetSkill(spec, pageable));
    }

    @DeleteMapping("/skills/{id}")
    @ApiMessage("delete skill by id")
    public ResponseEntity<Void> deleteSkill(@PathVariable("id") Long id) {
        this.skillService.deleteById(id);
        return ResponseEntity.ok().body(null);
    }
}
