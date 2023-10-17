package com.example.blogsystemtest.controller.blog;


import com.example.blogsystemtest.domain.ResponseResult;
import com.example.blogsystemtest.domain.dto.AddArticleDto;
import com.example.blogsystemtest.domain.dto.ArticleListDto;
import com.example.blogsystemtest.domain.dto.LikeDto;
import com.example.blogsystemtest.domain.dto.PageDto;
import com.example.blogsystemtest.domain.entity.Article;
import com.example.blogsystemtest.domain.vo.ArticleListVo;
import com.example.blogsystemtest.domain.vo.ArticleVo;
import com.example.blogsystemtest.service.ArticleService;
import com.example.blogsystemtest.utils.SecurityUtils;
import com.example.blogsystemtest.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


@RestController("blog-article-controller")
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList(){
        ResponseResult result = articleService.hotArticleList();
        return result;
    }

    @PostMapping("/articleList")
    public ResponseResult articleList(@RequestBody PageDto pageDto){
        return articleService.articleList(pageDto.getPageNum(),pageDto.getPageSize(),(Long) pageDto.getObject());
    }
    @PutMapping("/like/{id}")
    public ResponseResult like(@PathVariable Long id){
        Long userid= SecurityUtils.getUserId();
        LikeDto likeDto=new LikeDto(id,userid);
        return articleService.like(likeDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id){
        return articleService.getArticleDetail(id);
    }
    //TODO 这里要配置自动更新浏览量，自己发挥写一个点赞

    /**
     * 增加文章
     * @param articleDto
     * @return
     */
    @PostMapping("/add")
    public ResponseResult add(@RequestBody AddArticleDto articleDto){
        return articleService.add(articleDto);
    }

    /**
     * 更新文章
     * @param getArticleVo
     * @return
     */
    @PutMapping("/update")
    public ResponseResult update(@RequestBody ArticleVo getArticleVo){
        return articleService.update(getArticleVo);
    }

    @PostMapping ("/list")
    public ResponseResult list(@RequestBody PageDto pageDto){
        ArticleListDto articleListDto=new ArticleListDto();
        if(Objects.isNull(pageDto.getObject())){
            articleListDto.setUserId(SecurityUtils.getUserId());
        }else{
            articleListDto=(ArticleListDto) pageDto.getObject();
            articleListDto.setUserId(SecurityUtils.getUserId());
        }
        return articleService.pageArticleList(pageDto.getPageNum(),pageDto.getPageSize(),articleListDto);
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
