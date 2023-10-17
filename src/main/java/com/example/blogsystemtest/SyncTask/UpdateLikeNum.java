package com.example.blogsystemtest.SyncTask;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.blogsystemtest.domain.entity.Article;
import com.example.blogsystemtest.service.ArticleService;
import com.example.blogsystemtest.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class UpdateLikeNum {
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleService articleService;

    @Scheduled(cron = "0 0/1 * * * ?")
    @Async("taskExecutor")
    public void UpdateLikeNum(){
        //获取redis中的浏览量，更新到数据库中
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

        System.out.print("定时任务二");
    }
}
