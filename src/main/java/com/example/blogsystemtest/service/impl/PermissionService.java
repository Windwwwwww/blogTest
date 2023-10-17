package com.example.blogsystemtest.service.impl;


import com.example.blogsystemtest.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ps")
public class PermissionService {

    /**
     * //判断当前用户是否具有permission
     * @param permission 要判断的权限
     * @return
     */
    public boolean hasPermission(String permission){
        //如果是超级管理员直接返回true
        if (SecurityUtils.isAdmin()){
            return true;
        }
        List<String> permissions = SecurityUtils.getLoginUser().getPermissions();
        //否则 获取用户所具有的权限列表判断是否存在permission
        return permissions.contains(permission);
    }
}
