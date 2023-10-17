package com.example.blogsystemtest.domain.dto;

import lombok.Data;

@Data
public class PageDto {

    Integer pageNum;
    Integer pageSize;
    Object  object;

}
