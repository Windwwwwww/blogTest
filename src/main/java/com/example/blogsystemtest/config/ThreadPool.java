package com.example.blogsystemtest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;



@Configuration
@EnableAsync
public class ThreadPool{
    @Bean
    public ThreadPoolTaskExecutor getThreadPool(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程数
        executor.setCorePoolSize(10);
        //最大线程数
        executor.setMaxPoolSize(20);
        //阻塞队列
        executor.setQueueCapacity(500);
        //允许线程空闲时间
        executor.setKeepAliveSeconds(60);
        //线程池的前缀
        executor.setThreadNamePrefix("threadPool-");
        //拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        executor.initialize();
        return executor;
    }
}
