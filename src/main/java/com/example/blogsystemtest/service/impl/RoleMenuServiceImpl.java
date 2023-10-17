package com.example.blogsystemtest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blogsystemtest.domain.entity.RoleMenu;
import com.example.blogsystemtest.mapper.RoleMenuMapper;
import com.example.blogsystemtest.service.RoleMenuService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

    @Override
    public List<Long> getreMenuIds(Long id) {
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId,id);
        List<RoleMenu> list = getBaseMapper().selectList(queryWrapper);

        List<Long> menuList = new ArrayList<>();
        list.stream()
                .map(menuId->menuList.add(menuId.getMenuId()))
                .collect(Collectors.toList());

        return menuList;
    }
}
