package com.example.blogsystemtest.controller.admin;


import com.example.blogsystemtest.domain.ResponseResult;
import com.example.blogsystemtest.domain.dto.ArticleListDto;
import com.example.blogsystemtest.domain.dto.LinkListDto;
import com.example.blogsystemtest.domain.dto.PageDto;
import com.example.blogsystemtest.domain.vo.LinkVo;
import com.example.blogsystemtest.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController("admin-link-controller")
@RequestMapping("/content/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @PostMapping ("/list")
    public ResponseResult pageLinkList(@RequestBody PageDto pageDto){
        return linkService.pageLinkList(pageDto.getPageNum(),pageDto.getPageSize(),(LinkListDto) pageDto.getObject());
    }

    @PostMapping("/add")
    public ResponseResult addLink(@RequestBody LinkVo linkVo){
        return linkService.addLink(linkVo);
    }

    @GetMapping("/get/{id}")
    public ResponseResult getLink(@PathVariable String id){
        return linkService.getLink(id);
    }

    @PutMapping("/update")
    public ResponseResult updateLink(@RequestBody LinkVo linkVo){
        return linkService.update(linkVo);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseResult deleteLink(@PathVariable String id){
        return linkService.deleteLink(id);
    }
}
