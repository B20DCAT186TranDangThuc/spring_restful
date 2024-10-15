package com.dangthuc.job.springrestfulmaven.dto;

import com.dangthuc.job.springrestfulmaven.entity.Meta;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultPaginationDTO {
    private Meta meta;
    private Object result;
}
