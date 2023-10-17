package com.example.blogsystemtest.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCommentDto {


    //评论类型（0代表文章评论，1代表友链评论）

//    private String type;
    //文章id

    private Long articleId;
    //根评论id
    private Long rootId;
    //评论内容
    private String content;
    //所回复的目标评论的userid
    private Long toCommentUserId;
    //回复目标评论id
    private Long toCommentId;

    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;
}
