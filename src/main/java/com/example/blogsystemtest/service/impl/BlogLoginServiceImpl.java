package com.example.blogsystemtest.service.impl;


import com.example.blogsystemtest.domain.ResponseResult;
import com.example.blogsystemtest.domain.entity.LoginUser;
import com.example.blogsystemtest.domain.entity.User;
import com.example.blogsystemtest.domain.vo.BlogUserLoginVo;
import com.example.blogsystemtest.domain.vo.UserInfoVo;
import com.example.blogsystemtest.service.BlogLoginService;
import com.example.blogsystemtest.utils.BeanCopyUtils;
import com.example.blogsystemtest.utils.JwtUtil;
import com.example.blogsystemtest.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;



@Service
public class BlogLoginServiceImpl implements BlogLoginService {
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;
    @Override
    public ResponseResult login(User user) {
        // 构造用户名密码认证信息
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        // 对认证信息进行认证
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        //获取userID生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //把用户信息存入redis
        redisCache.setCacheObject("blogLogin:"+userId,loginUser);
        //token和userinfo封装返回

        //把User转换成UserinfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        BlogUserLoginVo vo = new BlogUserLoginVo(jwt,userInfoVo);

        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult logout() {
        //获取token解析userid
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //loginUser
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //获取userid
        Long userId = loginUser.getUser().getId();
        //删除redis中的用户信息
        redisCache.deleteObject("blogLogin:"+userId);
        return ResponseResult.okResult();
    }
}
