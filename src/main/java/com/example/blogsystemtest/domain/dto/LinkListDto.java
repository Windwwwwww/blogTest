package com.example.blogsystemtest.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 根据条件查询友链列表
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkListDto {
    private String name;
    //审核状态 (0代表审核通过，1代表审核未通过，2代表未审核)
    private String status;

}
