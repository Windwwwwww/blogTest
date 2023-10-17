package com.example.blogsystemtest.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 根据条件查询角色列表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleListDto {
    //角色名称
    private String roleName;
    //角色状态（0正常 1停用）
    private String status;
}
