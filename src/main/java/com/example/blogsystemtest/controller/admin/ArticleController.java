package com.example.blogsystemtest.controller.admin;



import com.example.blogsystemtest.domain.ResponseResult;
import com.example.blogsystemtest.domain.dto.AddArticleDto;
import com.example.blogsystemtest.domain.dto.ArticleListDto;
import com.example.blogsystemtest.domain.dto.PageDto;
import com.example.blogsystemtest.domain.vo.ArticleVo;
import com.example.blogsystemtest.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


@RestController("admin-article-controller")
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping ("/list")
    public ResponseResult list(@RequestBody PageDto pageDto){
        return articleService.pageArticleList(pageDto.getPageNum(),pageDto.getPageSize(),(ArticleListDto) pageDto.getObject());
    }

    @GetMapping("/get/{id}")
    public ResponseResult getArticle(@PathVariable("id") Long id){
        return articleService.getArticle(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseResult delete(@PathVariable("id") Long id){
        return articleService.deleteArticle(id);
    }
}
