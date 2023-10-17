package com.example.blogsystemtest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blogsystemtest.domain.ResponseResult;
import com.example.blogsystemtest.domain.dto.AddArticleDto;
import com.example.blogsystemtest.domain.dto.ArticleListDto;
import com.example.blogsystemtest.domain.dto.LikeDto;
import com.example.blogsystemtest.domain.entity.Article;
import com.example.blogsystemtest.domain.vo.ArticleVo;

public interface ArticleService extends IService<Article> {
    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);



    ResponseResult add(AddArticleDto article);

    ResponseResult pageArticleList(Integer pageNum, Integer pageSize, ArticleListDto articleListDto);

    ResponseResult getArticle(Long id);

    ResponseResult update(ArticleVo getArticleVo);

    ResponseResult deleteArticle(Long id);

    ResponseResult like(LikeDto likeDto);
}
