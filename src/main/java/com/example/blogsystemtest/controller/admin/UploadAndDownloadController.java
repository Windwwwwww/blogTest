package com.example.blogsystemtest.controller.admin;


import com.example.blogsystemtest.domain.ResponseResult;
import com.example.blogsystemtest.mapper.ArticleMapper;
import com.example.blogsystemtest.service.ArticleService;
import com.example.blogsystemtest.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


@RestController("admin-upload-controller")
@RequestMapping("/content")
public class UploadAndDownloadController {

    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    private UploadService uploadService;

    @PostMapping("/uploadImg")
    public ResponseResult uploadImg(@RequestParam("file") MultipartFile img){
        return uploadService.uploadImg(img);
    }
    @PostMapping("/uploadFile")
    public ResponseResult uploadFile(@RequestParam("file")MultipartFile file){
        return uploadService.uploadFile(file);
    }
    @GetMapping("/download/{id}")
    public void downloadFile(@PathVariable Long id, HttpServletResponse response) {
        String path=articleMapper.selectById(id).getFilePath();

        uploadService.download(path,response);
    }
}
