package com.example.blogsystemtest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blogsystemtest.domain.ResponseResult;
import com.example.blogsystemtest.domain.entity.User;
import com.example.blogsystemtest.domain.entity.UserRole;
import com.example.blogsystemtest.enums.AppHttpCodeEnum;
import com.example.blogsystemtest.mapper.RoleMapper;
import com.example.blogsystemtest.mapper.UserMapper;
import com.example.blogsystemtest.mapper.UserRoleMapper;
import com.example.blogsystemtest.service.RoleService;
import com.example.blogsystemtest.service.UserRoleService;
import com.example.blogsystemtest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Autowired
    RoleMapper roleMapper;
    @Autowired
    UserMapper userMapper;
    @Override
    public List<Long> getRoleIds(Long id) {

        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId,id);
        List<UserRole> list = list(queryWrapper);
        List<Long> roleList = new ArrayList<>();
        list.stream()
                .map(roleId->roleList.add(roleId.getRoleId()))
                .collect(Collectors.toList());
        return roleList;
    }

    @Override
    public ResponseResult changeUserRole(UserRole userRole) {
        updateById(userRole);
        String type=roleMapper.selectById(userRole.getRoleId()).getRoleKey();
        LambdaUpdateWrapper<User> lambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(User::getType,type)
                .eq(User::getId,userRole.getUserId());
        userMapper.update(null,lambdaUpdateWrapper);

        return ResponseResult.okResult();
    }
}
