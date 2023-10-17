package com.example.blogsystemtest.controller.admin;


import com.example.blogsystemtest.domain.ResponseResult;
import com.example.blogsystemtest.domain.entity.LoginUser;
import com.example.blogsystemtest.domain.entity.User;
import com.example.blogsystemtest.domain.vo.AdminUserInfoVo;
import com.example.blogsystemtest.domain.vo.MenuVo;
import com.example.blogsystemtest.domain.vo.RoutesVo;
import com.example.blogsystemtest.domain.vo.UserInfoVo;
import com.example.blogsystemtest.enums.AppHttpCodeEnum;
import com.example.blogsystemtest.exception.SystemException;
import com.example.blogsystemtest.service.AdminLoginService;
import com.example.blogsystemtest.service.MenuService;
import com.example.blogsystemtest.service.RoleService;
import com.example.blogsystemtest.utils.BeanCopyUtils;
import com.example.blogsystemtest.utils.RedisCache;
import com.example.blogsystemtest.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/system")
public class AdminLoginController {

    @Autowired
    private AdminLoginService adminLoginService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RedisCache redisCache;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if (!StringUtils.hasText(user.getUserName())){
            //提示必须要用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return adminLoginService.login(user);
    }

    @GetMapping("/getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
        //获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //根据用户id查询权限信息
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());
        //根据用户id查询角色信息
        List<String> roleKetList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());

        //获取用户信息
        User user = loginUser.getUser();
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        //封装返回
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms,roleKetList,userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }

    @GetMapping("/getRouters")
    public ResponseResult<RoutesVo> getRoutes() {
        Long userId = SecurityUtils.getUserId();
        //查询menu 结果是tree的形式
        List<MenuVo> menus = menuService.selectRouterMenuTreeByUserId(userId);
        //封装返回
        return ResponseResult.okResult(new RoutesVo(menus));
    }

    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return adminLoginService.logout();
    }
}
