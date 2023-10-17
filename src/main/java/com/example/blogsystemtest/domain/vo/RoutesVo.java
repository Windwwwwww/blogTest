package com.example.blogsystemtest.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 获取所有菜单列表（结果为树）
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoutesVo {
    private List<MenuVo> menus;

}
