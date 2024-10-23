package com.dangthuc.job.springrestfulmaven.entity;

import com.dangthuc.job.springrestfulmaven.util.SecurityUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "companies")
@Getter
@Setter
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "name khong duoc de trong")
    private String name;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    private String address;

    private String logo;

    //    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GTM+7")
    private Instant createAt;
    //    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GTM+7")
    private Instant updatedAt;
    private String createBy;
    private String updateBy;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    List<User> users;

    @PrePersist
    public void handleBeforeCreate() {
        this.createBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        this.createAt = Instant.now();
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updateBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        this.updatedAt = Instant.now();
    }


}
