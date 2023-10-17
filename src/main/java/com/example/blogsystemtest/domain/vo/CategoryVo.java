package com.example.blogsystemtest.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分类增删改查都可用该类
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryVo {
    private Long id;
    //描述
    private String description;
    //状态0:正常,1禁用
    private String status;
    //分类名
    private String name;
}
