package com.example.blogsystemtest.controller.blog;


import com.example.blogsystemtest.annotation.SystemLog;
import com.example.blogsystemtest.domain.ResponseResult;
import com.example.blogsystemtest.domain.dto.UpdateUserDto;
import com.example.blogsystemtest.domain.entity.User;
import com.example.blogsystemtest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController("blog-user-controller")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/userInfo")
    @SystemLog(BusinessName = "查看个人信息")
    public ResponseResult userInfo(){
        return userService.userInfo();
    }

    @PutMapping("/update")
    @SystemLog(BusinessName = "更新用户信息")
    public ResponseResult updateUserInfo(@RequestBody UpdateUserDto user){
        return userService.updateUserInfo(user);
    }

    @PostMapping("/register")
    @SystemLog(BusinessName = "用户注册")
    public ResponseResult register(@RequestBody User user){
        return userService.register(user);
    }
}
