package com.example.blogsystemtest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blogsystemtest.domain.ResponseResult;
import com.example.blogsystemtest.domain.dto.LinkListDto;
import com.example.blogsystemtest.domain.entity.Link;
import com.example.blogsystemtest.domain.vo.LinkVo;


public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult pageLinkList(Integer pageNum, Integer pageSize, LinkListDto linkListDto);

    ResponseResult addLink(LinkVo linkVo);

    ResponseResult getLink(String id);

    ResponseResult update(LinkVo linkVo);

    ResponseResult deleteLink(String id);
}
