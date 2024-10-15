package com.dangthuc.job.springrestfulmaven.dto;

import lombok.*;

@Getter
@Setter
@Builder
public class ResLoginDTO {
    private String accessToken;
    private UserLogin user;

    @Getter
    @Setter
    @Builder
    public static class UserLogin {
        private long id;
        private String email;
        private String name;
    }
}
