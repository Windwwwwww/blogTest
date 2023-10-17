package com.example.blogsystemtest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.blogsystemtest.domain.entity.Comment;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}
