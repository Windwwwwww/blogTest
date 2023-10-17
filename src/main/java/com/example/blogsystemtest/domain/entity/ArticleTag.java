package com.example.blogsystemtest.domain.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 文章标签关联表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("fe_article_tag")
public class ArticleTag implements Serializable {
    //文章id
    @TableId(type = IdType.INPUT)
    private Long articleId;
    //标签id

    private Long tagId;

}

