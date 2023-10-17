package com.example.blogsystemtest.domain.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色和菜单关联表(RoleMenu)
 *
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_role_menu")
public class RoleMenu {
    //角色ID
    @TableId(type = IdType.INPUT)
    //需要手动输入id，不自增
    private Long roleId;
    //菜单ID
    private Long menuId;




}

