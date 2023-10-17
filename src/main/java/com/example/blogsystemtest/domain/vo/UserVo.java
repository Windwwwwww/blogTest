package com.example.blogsystemtest.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * 详细个人信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {

    private Long id;

    //用户名
    private String userName;
    //昵称
    private String nickName;
    //账号状态（0正常 1停用）
    private String status;
    //邮箱
    private String email;
    //手机号
    private String phonenumber;
    //用户性别（0男，1女，2未知）
    private String sex;
    //头像
    private String avatar;
    //创建时间
    private Date createTime;
    //更新人
    private Long updateBy;
    //更新时间
    private Date updateTime;
}
