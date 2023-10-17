package com.example.blogsystemtest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blogsystemtest.domain.entity.ArticleTag;
import com.example.blogsystemtest.mapper.ArticleTagMapper;
import com.example.blogsystemtest.service.ArticleTagService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

    @Override
    public List<Long> getTagList(Long id) {
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId,id);
        queryWrapper.select(ArticleTag::getTagId);
        List<ArticleTag> list = list(queryWrapper);
        List<Long> longList = new ArrayList<>();
        for (ArticleTag articleTag : list) {
            Long tagId = articleTag.getTagId();
            longList.add(tagId);
        }
        return longList;
    }
}
