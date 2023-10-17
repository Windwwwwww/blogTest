package com.example.blogsystemtest.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 用户界面获取菜单
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class RoleMenuVo {

    private Long id;
    //菜单名称
    private String label;
    //父菜单ID
    private Long parentId;
    private List<RoleMenuVo> children;
}