package com.example.blogsystemtest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blogsystemtest.domain.entity.ArticleTag;

import java.util.List;



public interface ArticleTagService extends IService<ArticleTag> {

    List<Long> getTagList(Long id);
}
