package com.example.blogsystemtest.service.impl;


import com.example.blogsystemtest.domain.ResponseResult;
import com.example.blogsystemtest.domain.entity.LoginUser;
import com.example.blogsystemtest.domain.entity.User;
import com.example.blogsystemtest.enums.AppHttpCodeEnum;
import com.example.blogsystemtest.service.AdminLoginService;
import com.example.blogsystemtest.utils.JwtUtil;
import com.example.blogsystemtest.utils.RedisCache;
import com.example.blogsystemtest.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;



@Service
public class AdminLoginServiceImpl implements AdminLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
//    @Async("getThreadPool")
    public ResponseResult login(User user) {
        // 构造用户名密码认证信息
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        // 对认证信息进行认证
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        if(!(((LoginUser)authenticate.getPrincipal()).getUser().getType().equals("2"))){
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR);
        }
        //获取userID生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //把用户信息存入redis
        redisCache.setCacheObject("login:"+userId,loginUser);
        //token和userinfo封装返回

        Map<String,String> map = new HashMap<>();
        map.put("token",jwt);
//        CompletableFuture.completedFuture(map);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult logout() {
        //获取当前登录用户id
        Long userId = SecurityUtils.getUserId();
        //删除redis中对应的值
        redisCache.deleteObject("login:"+userId);
        return ResponseResult.okResult();
    }
}
