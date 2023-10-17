package com.example.blogsystemtest.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 根据条件查询标签列表
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagListDto {
    private String name;

    private String remark;
}
