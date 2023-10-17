package com.example.blogsystemtest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blogsystemtest.domain.ResponseResult;
import com.example.blogsystemtest.domain.dto.AddUserDto;
import com.example.blogsystemtest.domain.dto.ChangeUserDto;
import com.example.blogsystemtest.domain.dto.UpdateUserDto;
import com.example.blogsystemtest.domain.dto.UserListDto;
import com.example.blogsystemtest.domain.entity.User;
import com.example.blogsystemtest.domain.vo.PageVo;


public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(UpdateUserDto user);

    ResponseResult register(User user);

    ResponseResult<PageVo> pageUserList(Integer pageNum, Integer pageSize, UserListDto userListDto);

    ResponseResult addUser(AddUserDto addUserDto);

    ResponseResult deleteUser(Long id);

    User getUser(Long id);

    ResponseResult updateUser(UpdateUserDto updateUserDto);

    ResponseResult changeStatus(ChangeUserDto changeUserDto);
}
