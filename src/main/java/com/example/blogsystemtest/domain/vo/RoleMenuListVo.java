package com.example.blogsystemtest.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * 获取菜单列表（树以及可勾选选项id）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleMenuListVo {
    private List<RoleMenuVo> menus;

    private List checkedKeys;

}
