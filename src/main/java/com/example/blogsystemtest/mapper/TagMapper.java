package com.example.blogsystemtest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.blogsystemtest.domain.entity.Tag;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface TagMapper extends BaseMapper<Tag> {

}
