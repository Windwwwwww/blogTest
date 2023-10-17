package com.example.blogsystemtest.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 管理用户状态开关
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeUserDto {

    private Long userId;

    //角色状态（0正常 1停用）
    private String status;
}
