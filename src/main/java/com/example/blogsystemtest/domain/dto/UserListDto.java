package com.example.blogsystemtest.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 根据指定条件查询用户列表
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListDto {

    //用户名
    private String userName;
    //账号状态（0正常 1停用）
    private String status;
    //手机号
    private String phonenumber;
}
