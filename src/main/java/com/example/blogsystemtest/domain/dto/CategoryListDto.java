package com.example.blogsystemtest.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 根据条件查询种类列表
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryListDto {
    //分类名
    private String name;
    //状态0:正常,1禁用
    private String status;
}
