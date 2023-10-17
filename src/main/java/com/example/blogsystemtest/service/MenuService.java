package com.example.blogsystemtest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blogsystemtest.domain.ResponseResult;
import com.example.blogsystemtest.domain.dto.MenuDto;
import com.example.blogsystemtest.domain.entity.Menu;
import com.example.blogsystemtest.domain.vo.MenuListVo;
import com.example.blogsystemtest.domain.vo.MenuVo;


import java.util.List;



public interface MenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long id);


    List<MenuVo> selectRouterMenuTreeByUserId(Long userId);

    ResponseResult list(MenuDto menuDto);

    ResponseResult addMenu(MenuListVo menuListVo);

    ResponseResult getMenu(Long id);

    ResponseResult updateMenu(MenuListVo menuListVo);

    ResponseResult deleteMenu(Long menuId);
}
