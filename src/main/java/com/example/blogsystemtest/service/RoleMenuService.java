package com.example.blogsystemtest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blogsystemtest.domain.entity.RoleMenu;


import java.util.List;



public interface RoleMenuService extends IService<RoleMenu> {

    List<Long> getreMenuIds(Long id);
}
