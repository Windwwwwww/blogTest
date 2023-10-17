package com.example.blogsystemtest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blogsystemtest.domain.ResponseResult;
import com.example.blogsystemtest.domain.dto.TagListDto;
import com.example.blogsystemtest.domain.dto.UpdateTagDto;
import com.example.blogsystemtest.domain.entity.Tag;
import com.example.blogsystemtest.domain.vo.AllTagVo;
import com.example.blogsystemtest.domain.vo.PageVo;


public interface TagService extends IService<Tag> {


    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult addTag(Tag tag);

    ResponseResult deleteTag(Long id);

    ResponseResult getTag(Long id);

    ResponseResult updateTag(Tag tag);

    ResponseResult<AllTagVo> listAllTag();
}
