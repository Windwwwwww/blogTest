package com.example.blogsystemtest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blogsystemtest.domain.ResponseResult;
import com.example.blogsystemtest.domain.dto.AddRoleDto;
import com.example.blogsystemtest.domain.dto.ChangeRoleDto;
import com.example.blogsystemtest.domain.dto.RoleListDto;
import com.example.blogsystemtest.domain.dto.UpdateRoleDto;
import com.example.blogsystemtest.domain.entity.Menu;
import com.example.blogsystemtest.domain.entity.Role;
import com.example.blogsystemtest.domain.entity.RoleMenu;
import com.example.blogsystemtest.domain.vo.PageVo;
import com.example.blogsystemtest.domain.vo.RoleMenuVo;
import com.example.blogsystemtest.domain.vo.RoleVo;
import com.example.blogsystemtest.mapper.MenuMapper;
import com.example.blogsystemtest.mapper.RoleMapper;
import com.example.blogsystemtest.service.RoleMenuService;
import com.example.blogsystemtest.service.RoleService;
import com.example.blogsystemtest.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是管理员 是返回集合中只需要有admin
        if (id ==1L) {
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        //否则查询用户说具有的角色信息
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public ResponseResult<PageVo> pageRoleList(Integer pageNum, Integer pageSize, RoleListDto roleListDto) {
        //分页查询
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        if(Objects.nonNull(roleListDto)){
            queryWrapper.like(StringUtils.hasText(roleListDto.getRoleName()),Role::getRoleName,roleListDto.getRoleName());
            queryWrapper.eq(StringUtils.hasText(roleListDto.getStatus()),Role::getStatus,roleListDto.getStatus());
        }
        queryWrapper.orderByAsc(Role::getRoleSort);

        Page<Role> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        Page<Role> roleList = page(page, queryWrapper);
        //封装数据返回
        //封装成tagVo集合
        List<RoleVo> roleVo = BeanCopyUtils.copyBeanList(roleList.getRecords(), RoleVo.class);
        PageVo  pageVo = new PageVo(roleVo,roleList.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult changeStatus(ChangeRoleDto changeRoleDto) {
        Role role = getBaseMapper().selectById(changeRoleDto.getRoleId());
        role.setStatus(changeRoleDto.getStatus());
        updateById(role);
        return ResponseResult.okResult();
    }

    @Override
    public List<RoleMenuVo> getTreeselect() {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Menu::getOrderNum);
        List<Menu> menus = menuMapper.selectList(queryWrapper);
        List<RoleMenuVo> roleMenuVos = menus.stream()
                .map(m -> new RoleMenuVo(m.getId(), m.getMenuName(), m.getParentId(), null))
                .collect(Collectors.toList());
        //构建tree
        //先找出第一层菜单，然后再找子菜单，设置到children中
        List<RoleMenuVo> menuTree = buildMenuTree(roleMenuVos,-1L);
        return menuTree;
    }

    @Override
    @Transactional
    public ResponseResult addRole(AddRoleDto addRoleDto) {
        Role role = BeanCopyUtils.copyBean(addRoleDto, Role.class);
        save(role);

        List<RoleMenu> roleMenus = addRoleDto.getMenuIds().stream()
                .map(menuId -> new RoleMenu(role.getId(),menuId))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenus);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getRole(Long id) {
        Role role = getBaseMapper().selectById(id);
        RoleVo roleVo = BeanCopyUtils.copyBean(role, RoleVo.class);
        return ResponseResult.okResult(roleVo);
    }

    //根据id获得菜单树
/*    @Override
    public List<RoleMenuVo> getTreeselectById(Long id) {
        if (id.equals(1L)){
            List<Menu> menus = menuMapper.selectList(null);

            List<RoleMenuVo> roleMenuVos = menus.stream()
                    .map(m -> new RoleMenuVo(m.getId(), m.getMenuName(), m.getParentId(), null))
                    .collect(Collectors.toList());
            //构建tree
            //先找出第一层菜单，然后再找子菜单，设置到children中
            List<RoleMenuVo> menuTree = buildMenuTree(roleMenuVos,0L);
            return menuTree;
        }
        //根据id查出菜单集合
        LambdaQueryWrapper<RoleMenu> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(RoleMenu::getRoleId,id)
                .select(RoleMenu::getMenuId);
        List<RoleMenu> list = roleMenuService.list(queryWrapper2);
        //转换成单列集合
        List<Long> menulist = new ArrayList<>();
        list.stream()
                .map(menuId -> menulist.add(menuId.getMenuId()))
                .collect(Collectors.toList());

        List<Menu> menus = menuMapper.selectBatchIds(menulist);
        List<RoleMenuVo> roleMenuVos = menus.stream()
                .map(m -> new RoleMenuVo(m.getId(), m.getMenuName(), m.getParentId(), null))
                .collect(Collectors.toList());
        //构建tree
        //先找出第一层菜单，然后再找子菜单，设置到children中
        List<RoleMenuVo> menuTree = buildMenuTree(roleMenuVos,0L);
        return menuTree;
    }*/

    @Override
    public List getcheckedKeysList(Long id) {
        if (id.equals(1711650017433178113L)){
            List<Menu> list = menuMapper.selectList(null);

            //根据id查出菜单集合
            List<Long> menulist = new ArrayList<>();
            list.stream()
                    .map(menuId -> menulist.add(menuId.getId()))
                    .collect(Collectors.toList());
            return menulist;
        }
        //根据id查出菜单集合
        LambdaQueryWrapper<RoleMenu> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(RoleMenu::getRoleId,id);
        List<RoleMenu> list = roleMenuService.list(queryWrapper2);
        //转换成单列集合
        List<Long> menulist = new ArrayList<>();
        list.stream()
                .map(menuId -> menulist.add(menuId.getMenuId()))
                .collect(Collectors.toList());
        return menulist;
    }

    @Override
    @Transactional
    public ResponseResult deleteRole(Long id) {
        //删除角色
        removeById(id);
        //删除关联的菜单
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId,id);
        roleMenuService.remove(queryWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateRole(UpdateRoleDto updateRoleDto) {
        //更新role
        Role role = BeanCopyUtils.copyBean(updateRoleDto, Role.class);
        updateById(role);
        //更新role menu表
        //获取最新的菜单集合
        List<Long> menuIds = updateRoleDto.getMenuIds();
        //获取未更新之前的菜单集合
        List<Long> reMenuIds = roleMenuService.getreMenuIds(updateRoleDto.getId());

        List<RoleMenu> roleMenus = menuIds.stream()
                .filter(reMenuId -> !reMenuIds.contains(reMenuId))
                .map(reMenuId -> new RoleMenu(updateRoleDto.getId(), reMenuId))
                .collect(Collectors.toList());

                if (roleMenus.size()>0){
                    roleMenuService.saveBatch(roleMenus);
                }else if (roleMenus.size() == 0){
                    List<RoleMenu> roleMenus2 = reMenuIds.stream()
                            .filter(menuId->!menuIds.contains(menuId))
                            .map(menuId -> new RoleMenu(updateRoleDto.getId(), menuId))
                            .collect(Collectors.toList());
                    for (RoleMenu roleMenu : roleMenus2) {
                        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
                        queryWrapper.eq(RoleMenu::getRoleId,updateRoleDto.getId());
                        queryWrapper.eq(RoleMenu::getMenuId,roleMenu.getMenuId());
                        roleMenuService.remove(queryWrapper);
                    }
                }
        return null;
    }

    @Override
    public ResponseResult listAllRole() {
        List<Role> list = list(null);
        List<RoleVo> listAllRoleVos = BeanCopyUtils.copyBeanList(list, RoleVo.class);
        return ResponseResult.okResult(listAllRoleVos);
    }

    @Override
    public List<RoleVo> getRoles() {
        List<Role> list = list(null);
        List<RoleVo> listAllRoleVos = BeanCopyUtils.copyBeanList(list, RoleVo.class);
        return listAllRoleVos;
    }

    /**
     * 构建树
     * @param menus
     * @param parentId
     * @return
     */
    private List<RoleMenuVo> buildMenuTree(List<RoleMenuVo> menus, Long parentId) {
        //只要id为o的
        List<RoleMenuVo> muenTree = menus.stream()
                .filter(menuVo -> menuVo.getParentId().equals(parentId))
                .map(menuVo -> menuVo.setChildren(getChildren(menuVo,menus)))
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
    private List<RoleMenuVo> getChildren(RoleMenuVo menuVo, List<RoleMenuVo> menus) {
        List<RoleMenuVo> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menuVo.getId()))
                .map(m ->m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
        return childrenList;
    }
}
