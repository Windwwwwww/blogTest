package com.example.blogsystemtest.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blogsystemtest.domain.ResponseResult;
import com.example.blogsystemtest.domain.dto.AddCommentDto;
import com.example.blogsystemtest.domain.entity.Comment;
import com.example.blogsystemtest.domain.vo.CommentVo;
import com.example.blogsystemtest.domain.vo.PageVo;
import com.example.blogsystemtest.enums.AppHttpCodeEnum;
import com.example.blogsystemtest.enums.SystemConstants;
import com.example.blogsystemtest.exception.SystemException;
import com.example.blogsystemtest.mapper.CommentMapper;
import com.example.blogsystemtest.service.CommentService;
import com.example.blogsystemtest.service.UserService;
import com.example.blogsystemtest.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;


@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserService userService;
    @Override
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize) {
        //查询对应文章的根评论

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        //对article进行判断
        queryWrapper.eq(Comment::getArticleId,articleId);
        //根评论  rootId为-1
        queryWrapper.eq(Comment::getRootId,-1);


        queryWrapper.orderByDesc(Comment::getCreateTime);
        //分页查询
        Page<Comment> page = new Page(pageNum,pageSize);

        List<CommentVo> commentVoList = toCommentVoList(page(page,queryWrapper).getRecords());

        //查询所有根评论对应的子评论集合，并且赋值给对应的属性
        for (CommentVo commentVo : commentVoList) {
            //查询对应的子评论
            List<CommentVo> children = getChildren(commentVo.getId());
            if(children.size()==0){
                continue;
            }
            //赋值
            commentVo.setChildren(children);
        }

        return ResponseResult.okResult(new PageVo(commentVoList,page.getTotal()));
    }

    @Override
    public ResponseResult addComment(AddCommentDto addCommentDto) {
        //评论内容不能为空
        if (!StringUtils.hasText(addCommentDto.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        Comment comment=BeanCopyUtils.copyBean(addCommentDto,Comment.class);
        save(comment);
        return ResponseResult.okResult();
    }

    /**
     * 根据根评论id查询所对应子评论集合
     * @param id 根评论的id
     * @return
     */
    private List<CommentVo> getChildren(Long id){
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId,id);
        queryWrapper.orderByDesc(Comment::getCreateTime);
        List<Comment> comments = list(queryWrapper);
        List<CommentVo> commentVos = toCommentVoList(comments);
        return commentVos;
    }

    private List<CommentVo> toCommentVoList(List<Comment> list){
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        //遍历Vo集合
        for (CommentVo commentVo : commentVos) {
            //通过createBy查询昵称并赋值
            String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
            commentVo.setUsername(nickName);
            //通过toCommentUserId查询昵称并赋值
            //如果toCommentUserId不为-1才查询
            if (commentVo.getToCommentId()!=-1){
                String toCommentUserName = userService.getById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(toCommentUserName);
            }
        }
        return commentVos;
    }
}
