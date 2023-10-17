package com.example.blogsystemtest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blogsystemtest.domain.ResponseResult;
import com.example.blogsystemtest.domain.dto.AddRoleDto;
import com.example.blogsystemtest.domain.dto.ChangeRoleDto;
import com.example.blogsystemtest.domain.dto.RoleListDto;
import com.example.blogsystemtest.domain.dto.UpdateRoleDto;
import com.example.blogsystemtest.domain.entity.Role;
import com.example.blogsystemtest.domain.vo.PageVo;
import com.example.blogsystemtest.domain.vo.RoleMenuVo;
import com.example.blogsystemtest.domain.vo.RoleVo;


import java.util.List;



public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult<PageVo> pageRoleList(Integer pageNum, Integer pageSize, RoleListDto roleListDto);

    ResponseResult changeStatus(ChangeRoleDto changeRoleVo);

    List<RoleMenuVo> getTreeselect();

    ResponseResult addRole(AddRoleDto addRoleDto);

    ResponseResult getRole(Long id);

//根据id获得菜单树
/*    List<RoleMenuVo> getTreeselectById(Long id);*/

    List getcheckedKeysList(Long id);

    ResponseResult deleteRole(Long id);

    ResponseResult updateRole(UpdateRoleDto updateRoleDto);

    ResponseResult listAllRole();

    List<RoleVo> getRoles();
}
