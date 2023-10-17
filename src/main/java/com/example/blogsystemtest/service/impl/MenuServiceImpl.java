package com.example.blogsystemtest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blogsystemtest.domain.ResponseResult;
import com.example.blogsystemtest.domain.dto.MenuDto;
import com.example.blogsystemtest.domain.entity.Menu;
import com.example.blogsystemtest.domain.vo.MenuListVo;
import com.example.blogsystemtest.domain.vo.MenuVo;
import com.example.blogsystemtest.enums.AppHttpCodeEnum;
import com.example.blogsystemtest.enums.SystemConstants;
import com.example.blogsystemtest.mapper.MenuMapper;
import com.example.blogsystemtest.service.MenuService;
import com.example.blogsystemtest.utils.BeanCopyUtils;
import com.example.blogsystemtest.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;
    @Override
    public List<String> selectPermsByUserId(Long id) {
        //如果是管理员返回所有权限
        if (SecurityUtils.isAdmin()){

            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Menu::getMenuType, SystemConstants.MENU,SystemConstants.BUTTON);
            wrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
            List<Menu> menus = list(wrapper);
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
        //否则返回其所具有的权限
        return menuMapper.selectPermsByUserId(id);
    }

    @Override
    public List<MenuVo> selectRouterMenuTreeByUserId(Long userId) {

        MenuMapper menumapper = getBaseMapper();
        List<MenuVo> menus = null;

        //判断是否是管理员
        if (SecurityUtils.isAdmin()){
            //如果是返回所有符合的menu
            menus = menumapper.selectAllRouter();
        }else {
            //查询对应的menu
            menus = menumapper.selectRouterMenuTreeByUserId(userId);
        }
        //构建tree

        //先找出第一层菜单，然后再找子菜单，设置到children中
        List<MenuVo> menuTree = buildMenuTree(menus,-1L);
        return menuTree;
    }

    @Override
    public ResponseResult list(MenuDto menuDto) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(menuDto.getStatus()),Menu::getStatus,menuDto.getStatus());
        queryWrapper.like(StringUtils.hasText(menuDto.getMenuName()),Menu::getMenuName,menuDto.getMenuName());
        queryWrapper.orderByAsc(Menu::getParentId);
        queryWrapper.orderByAsc(Menu::getOrderNum);
        List<Menu> list = list(queryWrapper);
        //封装数据返回
        //封装成tagVo集合
        List<MenuListVo> menuListVos = BeanCopyUtils.copyBeanList(list, MenuListVo.class);

        return ResponseResult.okResult(menuListVos);
    }

    @Override
    public ResponseResult addMenu(MenuListVo menuListVo) {
        Menu menu = BeanCopyUtils.copyBean(menuListVo, Menu.class);
        save(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getMenu(Long id) {
        Menu menu = getById(id);
        MenuListVo menuListVo = BeanCopyUtils.copyBean(menu, MenuListVo.class);
        return ResponseResult.okResult(menuListVo);
    }

    @Override
    public ResponseResult updateMenu(MenuListVo menuListVo) {
        Menu menu = BeanCopyUtils.copyBean(menuListVo, Menu.class);
        Menu menu1 = menuMapper.selectById(menuListVo.getId());
        if (Objects.equals(menu.getParentId(), menu1.getId())){//上级菜单不支持更改
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),"修改菜单失败，上级菜单不能选择自己");
        }
        updateById(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteMenu(Long menuId) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId,menuId);
        List<Menu> list = list(queryWrapper);
        if (list.size()!=0){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),"存在子菜单不允许删除");
        }
        removeById(menuId);
        return ResponseResult.okResult();
    }

    private List<MenuVo> buildMenuTree(List<MenuVo> menus, Long parentId) {
        List<MenuVo> muenTree = menus.stream()
                .filter(menuVo -> menuVo.getParentId().equals(parentId))//筛选根节点
                .map(menuVo -> menuVo.setChildren(getChildren(menuVo, menus)))
                .collect(Collectors.toList());
        return muenTree;
    }


    /**
     * 获取传入参数的子menu集合
     *
     * @param menuVo
     * @param menus
     * @return
     */
    private List<MenuVo> getChildren(MenuVo menuVo, List<MenuVo> menus) {
        List<MenuVo> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menuVo.getId()))//第二层节点与根节匹配
                .map(m ->m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
        return childrenList;
    }
}
