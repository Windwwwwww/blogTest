package com.example.blogsystemtest.domain.vo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 获取用户关联角色
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVoList {
    private List<Long> roleIds;
    private List<RoleVo> roles;
    private UserInfoVo user;
}
