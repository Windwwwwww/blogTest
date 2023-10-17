package com.example.blogsystemtest.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelStudentVo {


    @ExcelProperty("id")
    private Long id;

    //用户名
    @ExcelProperty("用户名")
    private String userName;
    //昵称
    @ExcelProperty("昵称")
    private String nickName;
    @ExcelProperty("状态")
    //状态0:正常,1禁用
    private String status;

    //邮箱
    @ExcelProperty("邮箱")
    private String email;
    //手机号
    @ExcelProperty("电话号码")
    private String phonenumber;
    //用户性别（0男，1女，2未知）
    @ExcelProperty("性别")
    private String sex;

}