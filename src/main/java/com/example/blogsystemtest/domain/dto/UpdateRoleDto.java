package com.example.blogsystemtest.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 更新角色
 * 相较addRoleDto增加id
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoleDto {

    private Long id;
    //角色名称
    private String roleName;
    //角色权限字符串
    private String roleKey;
    //显示顺序
    private Integer roleSort;
    //角色状态（0正常 1停用）
    private String status;

    private List<Long> menuIds;

    //备注
    private String remark;
}
