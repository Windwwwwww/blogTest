package com.example.blogsystemtest.controller.admin;


import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.blogsystemtest.domain.ResponseResult;
import com.example.blogsystemtest.domain.dto.*;
import com.example.blogsystemtest.domain.entity.User;
import com.example.blogsystemtest.domain.vo.*;
import com.example.blogsystemtest.enums.AppHttpCodeEnum;
import com.example.blogsystemtest.enums.SystemConstants;
import com.example.blogsystemtest.mapper.UserMapper;
import com.example.blogsystemtest.service.RoleService;
import com.example.blogsystemtest.service.UserRoleService;
import com.example.blogsystemtest.service.UserService;
import com.example.blogsystemtest.utils.BeanCopyUtils;
import com.example.blogsystemtest.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;



@RestController("admin-user-controller")
@RequestMapping("/system/user")
public class UserController {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @PostMapping("/list")
    public ResponseResult<PageVo> list(@RequestBody PageDto pageDto){
        return userService.pageUserList(pageDto.getPageNum(),pageDto.getPageSize(),(UserListDto) pageDto.getObject());
    }

    @PostMapping("/add")
    public ResponseResult addUser(@RequestBody AddUserDto addUserDto){
        return userService.addUser(addUserDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseResult deleteUser(@PathVariable Long id){
        return userService.deleteUser(id);
    }

    @GetMapping("/getUserInfo/{id}")
    public ResponseResult<UserVoList> getUserInfo(@PathVariable Long id){
        //获取用户关联角色列表
        List<Long> roleIds = userRoleService.getRoleIds(id);
        //获取所有角色信息
        List<RoleVo> roles = roleService. getRoles();
        //获取用户信息
        User reUser = userService.getUser(id);

        UserInfoVo user = BeanCopyUtils.copyBean(reUser, UserInfoVo.class);
        return ResponseResult.okResult(new UserVoList(roleIds,roles,user));
    }

    @PutMapping("/update")
    public ResponseResult updateUser(@RequestBody UpdateUserDto updateUserDto){
        return userService.updateUser(updateUserDto);
    }

    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody ChangeUserDto changeUserDto){
        return userService.changeStatus(changeUserDto);
    }

    @PreAuthorize("@ps.hasPermission('system:user:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        //设置下载文件的请求头
        try {
            WebUtils.setDownLoadHeader("学生.xlsx",response);

            LambdaQueryWrapper<User> lambdaQueryWrapper=new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getType,"0")
                    .eq(User::getDelFlag, "0");

            //获取需要导出的数据
            List<User> users=userMapper.selectList(lambdaQueryWrapper);

            List<ExcelStudentVo> excelStudentVos = BeanCopyUtils.copyBeanList(users, ExcelStudentVo.class);
            //写入Excel中
            EasyExcel.write(response.getOutputStream(), ExcelStudentVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelStudentVos);
        } catch (Exception e) {
            e.printStackTrace();
            //出现异常也要响应json
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }
}
