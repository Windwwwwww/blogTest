package com.example.blogsystemtest.controller.admin;


import com.example.blogsystemtest.domain.ResponseResult;
import com.example.blogsystemtest.domain.dto.*;
import com.example.blogsystemtest.domain.entity.UserRole;
import com.example.blogsystemtest.domain.vo.PageVo;
import com.example.blogsystemtest.domain.vo.RoleMenuListVo;
import com.example.blogsystemtest.domain.vo.RoleMenuVo;
import com.example.blogsystemtest.service.RoleService;
import com.example.blogsystemtest.service.UserRoleService;
import com.example.blogsystemtest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController()
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleService userRoleService;

    @PostMapping ("/list")
    public ResponseResult<PageVo> list(@RequestBody PageDto pageDto){
        return roleService.pageRoleList(pageDto.getPageNum(),pageDto.getPageSize(),(RoleListDto) pageDto.getObject());
    }

    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody ChangeRoleDto changeRoleDto) {
        return roleService.changeStatus(changeRoleDto);
    }

    @GetMapping("/treeselect")
    public ResponseResult treeselect(){
        //查询menu 结果是tree的形式
        List<RoleMenuVo> menus = roleService.getTreeselect();
        //封装返回
        return ResponseResult.okResult(menus);
    }

    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult getTreeselectById(@PathVariable Long id){
        //查询menu 结果是tree的形式
        List<RoleMenuVo> menus = roleService.getTreeselect();

        List list = roleService.getcheckedKeysList(id);
        //封装返回
        return ResponseResult.okResult(new RoleMenuListVo(menus,list));
    }

    @PostMapping("/add")
    public ResponseResult addRole(@RequestBody AddRoleDto addRoleDto){
        return roleService.addRole(addRoleDto);
    }

    @GetMapping("/get/{id}")
    public ResponseResult getRole(@PathVariable Long id){
        return roleService.getRole(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseResult deleteRole(@PathVariable Long id){
        return roleService.deleteRole(id);
    }

    @PutMapping("/update")
    public ResponseResult updateRole(@RequestBody UpdateRoleDto updateRoleDto){
        return roleService.updateRole(updateRoleDto);
    }

    @PutMapping("/changeRole")
    public ResponseResult changeRole(@RequestBody UserRole userRole){
        return userRoleService.changeUserRole(userRole);
    }

    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        return roleService.listAllRole();
    }
}
