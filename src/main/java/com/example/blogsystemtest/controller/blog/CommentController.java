package com.example.blogsystemtest.controller.blog;


import com.example.blogsystemtest.annotation.SystemLog;
import com.example.blogsystemtest.domain.ResponseResult;
import com.example.blogsystemtest.domain.dto.AddCommentDto;
import com.example.blogsystemtest.domain.dto.ArticleListDto;
import com.example.blogsystemtest.domain.dto.PageDto;
import com.example.blogsystemtest.domain.entity.Comment;
import com.example.blogsystemtest.enums.SystemConstants;
import com.example.blogsystemtest.service.CommentService;
import com.example.blogsystemtest.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;
    @PostMapping("/commentList")
    public ResponseResult commentList(@RequestBody PageDto pageDto){
        return commentService.commentList(Long.parseLong(pageDto.getObject().toString()),pageDto.getPageNum(),pageDto.getPageSize());
    }

    @PostMapping("/add")
    @SystemLog(BusinessName = "增加评论")
    public ResponseResult addComment(@RequestBody AddCommentDto addCommentDto){

        return commentService.addComment(addCommentDto);
    }


}
