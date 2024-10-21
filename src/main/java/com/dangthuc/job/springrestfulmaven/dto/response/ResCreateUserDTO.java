package com.dangthuc.job.springrestfulmaven.dto.response;

import com.dangthuc.job.springrestfulmaven.util.ENUM.GenderEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class ResCreateUserDTO {
    private long id;
    private String name;
    private String email;
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant createdAt;
}
