package com.dangthuc.job.springrestfulmaven.entity;

import lombok.Data;

@Data
public class RestResponse<T> {
    private int statusCode;
    private String error;
    private Object message;
    private T data;


}
