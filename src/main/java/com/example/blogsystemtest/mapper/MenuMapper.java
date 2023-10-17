package com.example.blogsystemtest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.blogsystemtest.domain.vo.MenuVo;
import com.example.blogsystemtest.domain.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> selectPermsByUserId(Long userId);

    List<MenuVo> selectAllRouter();

    List<MenuVo> selectRouterMenuTreeByUserId(Long userId);
}
