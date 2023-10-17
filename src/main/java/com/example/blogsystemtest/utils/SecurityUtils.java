package com.example.blogsystemtest.utils;

import com.example.blogsystemtest.domain.entity.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils
{

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser()
    {
        return (LoginUser) getAuthentication().getPrincipal();
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static Boolean isAdmin(){
        Long id = getLoginUser().getUser().getId();
        String type=getLoginUser().getUser().getType();
        return id != null && type.equals("2");
    }

    public static Long getUserId() {
        return getLoginUser().getUser().getId();
    }
}
