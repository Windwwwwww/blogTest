package com.example.blogsystemtest.controller.admin;


import com.example.blogsystemtest.domain.ResponseResult;
import com.example.blogsystemtest.domain.dto.*;
import com.example.blogsystemtest.domain.entity.Tag;
import com.example.blogsystemtest.domain.vo.AllTagVo;
import com.example.blogsystemtest.domain.vo.PageVo;
import com.example.blogsystemtest.service.TagService;
import com.example.blogsystemtest.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @PostMapping("/list")
    public ResponseResult<PageVo> list(@RequestBody PageDto pageDto){
        return tagService.pageTagList(pageDto.getPageNum(),pageDto.getPageSize(),(TagListDto) pageDto.getObject());
    }

    @GetMapping("/listAllTag")
    public ResponseResult<AllTagVo> listAllTag(){
        return tagService.listAllTag();
    }

    @PostMapping("/add")
    public ResponseResult addTag(@RequestBody AddTagDto addTagDto){
        Tag tag = BeanCopyUtils.copyBean(addTagDto, Tag.class);
        return tagService.addTag(tag);
    }

    @PutMapping("/update")
    public ResponseResult updateTag(@RequestBody UpdateTagDto updateTagDto){
        Tag tag = BeanCopyUtils.copyBean(updateTagDto, Tag.class);
        return tagService.updateTag(tag);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseResult deleteTag(@PathVariable("id") Long id){
        return tagService.deleteTag(id);
    }

    @GetMapping("/get/{id}")
    public ResponseResult getTag(@PathVariable("id") Long id){
        return tagService.getTag(id);
    }
}
