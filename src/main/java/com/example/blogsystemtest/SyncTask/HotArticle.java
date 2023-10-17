package com.example.blogsystemtest.SyncTask;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.blogsystemtest.domain.entity.Article;
import com.example.blogsystemtest.domain.vo.ArticleListVo;
import com.example.blogsystemtest.domain.vo.HotArticleVo;
import com.example.blogsystemtest.enums.SystemConstants;
import com.example.blogsystemtest.mapper.ArticleMapper;
import com.example.blogsystemtest.service.ArticleService;
import com.example.blogsystemtest.utils.BeanCopyUtils;
import com.example.blogsystemtest.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class HotArticle {
    @Autowired
    RedisCache redisCache;
    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    ArticleService articleService;

    @Autowired
    RedisTemplate redisTemplate;
    @Scheduled(cron = "0 0/1 * * * ?") // 每隔一分钟执行一次
    @Async("taskExecutor")
    public void syncData() {
    //获取数据库文章列表
        LambdaQueryWrapper<Article> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> result=articleMapper.selectList(lambdaQueryWrapper);
        List<HotArticleVo> list= BeanCopyUtils.copyBeanList(result,HotArticleVo.class);
        System.out.print(list);
        //因为是zset没法做id标识，所以每次更新需要清空一下
        redisTemplate.opsForZSet().removeRange("article:hot",0,-1);
            //缓存文章进zset，score值为浏览量
        for (HotArticleVo row : list) {
            Long viewCount = Long.parseLong(row.getViewCount().toString());

            redisCache.setCacheZSetValue("article:hot",row,viewCount);
        }

        System.out.print("定时任务一");
    }
}
