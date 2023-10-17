package com.example.blogsystemtest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blogsystemtest.domain.ResponseResult;
import com.example.blogsystemtest.domain.dto.TagListDto;
import com.example.blogsystemtest.domain.dto.UpdateTagDto;
import com.example.blogsystemtest.domain.entity.Tag;
import com.example.blogsystemtest.domain.vo.AllTagVo;
import com.example.blogsystemtest.domain.vo.PageVo;
import com.example.blogsystemtest.enums.AppHttpCodeEnum;
import com.example.blogsystemtest.exception.SystemException;
import com.example.blogsystemtest.mapper.TagMapper;
import com.example.blogsystemtest.service.TagService;
import com.example.blogsystemtest.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;


@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        //分页查询
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        if(Objects.nonNull(tagListDto)){
            queryWrapper.eq(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName());
            queryWrapper.eq(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());
        }

        Page<Tag> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        Page<Tag> tagList = page(page, queryWrapper);
        //封装数据返回
        //封装成tagVo集合
        List<UpdateTagDto> tagVo = BeanCopyUtils.copyBeanList(tagList.getRecords(), UpdateTagDto.class);
        PageVo  pageVo = new PageVo(tagVo,tagList.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addTag(Tag tag) {
        //标签名不能为空
        if (!StringUtils.hasText(tag.getName())){
            throw new SystemException(AppHttpCodeEnum.NAME_NOT_NULL);
        }
        save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteTag(Long id) {
        getBaseMapper().deleteById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getTag(Long id) {
        getBaseMapper().selectById(id);
        UpdateTagDto tagVo = BeanCopyUtils.copyBean(getBaseMapper().selectById(id), UpdateTagDto.class);
        return ResponseResult.okResult(tagVo);
    }

    @Override
    public ResponseResult updateTag(Tag tag) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tag::getId,tag.getId());
        update(tag,queryWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<AllTagVo> listAllTag() {
        List<Tag> list = list();
        List<AllTagVo> tagVos = BeanCopyUtils.copyBeanList(list, AllTagVo.class);
        return ResponseResult.okResult(tagVos);
    }
}
