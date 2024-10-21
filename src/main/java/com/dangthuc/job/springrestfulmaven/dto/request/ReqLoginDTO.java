package com.dangthuc.job.springrestfulmaven.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReqLoginDTO {
    @NotBlank(message = "username không được để trống")
    private String username;

    @NotBlank(message = "password không được để trống")
    private String password;
}
