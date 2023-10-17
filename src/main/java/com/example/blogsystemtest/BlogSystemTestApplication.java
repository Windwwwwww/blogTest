package com.example.blogsystemtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BlogSystemTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogSystemTestApplication.class, args);
    }

}
