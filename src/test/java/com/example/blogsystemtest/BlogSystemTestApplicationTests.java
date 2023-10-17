package com.example.blogsystemtest;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.blogsystemtest.domain.dto.AddArticleDto;
import com.example.blogsystemtest.domain.entity.Article;
import com.example.blogsystemtest.domain.entity.Category;
import com.example.blogsystemtest.domain.entity.Menu;
import com.example.blogsystemtest.domain.entity.Tag;
import com.example.blogsystemtest.domain.vo.CategoryVo;
import com.example.blogsystemtest.enums.SystemConstants;
import com.example.blogsystemtest.mapper.ArticleMapper;
import com.example.blogsystemtest.mapper.CategoryMapper;
import com.example.blogsystemtest.service.*;
import com.example.blogsystemtest.utils.BeanCopyUtils;
import com.example.blogsystemtest.utils.RedisCache;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SpringBootTest
class BlogSystemTestApplicationTests {
    @Autowired
    RedisCache redisCache;

    @Autowired
    AdminLoginService adminLoginService;
    @Autowired
    ArticleService articleService;
    @Autowired
    ArticleTagService articleTagService;
    @Autowired
    BlogLoginService blogLoginService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    CommentService commentService;
    @Autowired
    MenuService menuService;
    @Autowired
    RoleMenuService roleMenuService;
    @Autowired
    RoleService roleService;
    @Autowired
    TagService tagService;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    UserService userService;

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    ArticleMapper articleMapper;
    @Test
    void contextLoads() {

        redisCache.setCacheObject("test","1");

    }
    @Test
    void  test1(){
        Tag tag=new Tag();
        tag.setName("1");
        tag.setCreateBy(1L);
        tag.setCreateTime(new Date());

        tagService.addTag(tag);
    }

    @Test
    void  test2(){
        Category category=new Category();
        category.setName("java");
        category.setCreateBy(1L);
        category.setCreateTime(new Date());
        //CategoryVo categoryVo= BeanCopyUtils.copyBean(category,CategoryVo.class);
        categoryMapper.insert(category);

    }

    @Test
    void  test3(){

        LambdaQueryWrapper<Article> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> result=articleMapper.selectList(lambdaQueryWrapper);
        System.out.print(result);
    }

    @Test
    void test(){
        Map<String, Integer> likeNumMap = redisCache.getCacheMap("article:likeNum");
        if(Objects.isNull(likeNumMap)&&likeNumMap.isEmpty()){
            return;
        }
        for (Map.Entry<String,Integer> entry : likeNumMap.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            LambdaUpdateWrapper<Article> lambdaUpdateWrapper=new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.set(Article::getLikeNum,value.longValue())
                    .eq(Article::getId,Long.parseLong(key));
            articleService.update(null,lambdaUpdateWrapper);
        }
    }

}
