package com.example.blogsystemtest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blogsystemtest.domain.ResponseResult;
import com.example.blogsystemtest.domain.dto.LinkListDto;
import com.example.blogsystemtest.domain.entity.Link;
import com.example.blogsystemtest.domain.vo.LinkVo;
import com.example.blogsystemtest.domain.vo.PageVo;
import com.example.blogsystemtest.enums.SystemConstants;
import com.example.blogsystemtest.mapper.LinkMapper;
import com.example.blogsystemtest.service.LinkService;
import com.example.blogsystemtest.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;


@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {
    @Override
    public ResponseResult getAllLink() {
        //查询所有审核通过的
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> list = list(queryWrapper);
        //转换成vo
        List<LinkVo> LinkVo = BeanCopyUtils.copyBeanList(list, LinkVo.class);
        //封装返回
        return ResponseResult.okResult(LinkVo);
    }

    @Override
    public ResponseResult pageLinkList(Integer pageNum, Integer pageSize, LinkListDto linkListDto) {
        //分页查询
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(linkListDto.getName()),Link::getName,linkListDto.getName());
        queryWrapper.eq(StringUtils.hasText(linkListDto.getStatus()),Link::getStatus,linkListDto.getStatus());

        Page<Link> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        Page<Link> linkPage = page(page, queryWrapper);
        //封装数据返回
        //封装成tagVo集合
        List<LinkVo> categoryVos = BeanCopyUtils.copyBeanList(linkPage.getRecords(), LinkVo.class);
        PageVo pageVo = new PageVo(categoryVos,linkPage.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addLink(LinkVo linkVo) {
        Link link = BeanCopyUtils.copyBean(linkVo, Link.class);
        save(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getLink(String id) {
        Link link = getById(id);
        LinkVo linkVo = BeanCopyUtils.copyBean(link, LinkVo.class);
        return ResponseResult.okResult(linkVo);
    }

    @Override
    public ResponseResult update(LinkVo linkVo) {
        Link link = BeanCopyUtils.copyBean(linkVo, Link.class);
        updateById(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteLink(String id) {
        removeById(id);
        return ResponseResult.okResult();
    }
}
