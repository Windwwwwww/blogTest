package com.example.blogsystemtest.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 选择文章种类小菜单
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllCategoryVo {

    private Long id;

    //分类名
    private String name;

    //描述
    private String description;
}
