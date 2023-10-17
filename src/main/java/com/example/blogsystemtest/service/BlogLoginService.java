package com.example.blogsystemtest.service;


import com.example.blogsystemtest.domain.ResponseResult;
import com.example.blogsystemtest.domain.entity.User;


public interface BlogLoginService {

    ResponseResult login(User user);

    ResponseResult logout();
}
