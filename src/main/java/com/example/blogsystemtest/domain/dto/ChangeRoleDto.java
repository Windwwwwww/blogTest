package com.example.blogsystemtest.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 管理角色状态开关
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeRoleDto {

    private Long roleId;

    //角色状态（0正常 1停用）
    private String status;
}
