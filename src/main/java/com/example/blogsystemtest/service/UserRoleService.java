package com.example.blogsystemtest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blogsystemtest.domain.ResponseResult;
import com.example.blogsystemtest.domain.entity.UserRole;


import java.util.List;



public interface UserRoleService extends IService<UserRole> {

    List<Long> getRoleIds(Long id);
    ResponseResult changeUserRole(UserRole userRole);
}
