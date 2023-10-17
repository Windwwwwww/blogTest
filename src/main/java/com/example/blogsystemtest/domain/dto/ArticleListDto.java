package com.example.blogsystemtest.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 查询根据条件查询文章
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleListDto {

    //标题
    private String title;
    //文章摘要
    private String summary;

    private Long userId;
}
