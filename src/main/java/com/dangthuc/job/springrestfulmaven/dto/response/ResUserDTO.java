package com.dangthuc.job.springrestfulmaven.dto.response;

import com.dangthuc.job.springrestfulmaven.util.ENUM.GenderEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class ResUserDTO {
    private long id;
    private String email;
    private String name;
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant updatedAt;
    private Instant createdAt;

    private CompanyUser companyUser;

    @Getter
    @Setter
    public static class CompanyUser {
        private long id;
        private String name;
    }
}
