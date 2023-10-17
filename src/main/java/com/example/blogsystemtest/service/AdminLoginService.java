package com.example.blogsystemtest.service;


import com.example.blogsystemtest.domain.ResponseResult;
import com.example.blogsystemtest.domain.entity.User;

public interface AdminLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
