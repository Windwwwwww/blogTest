package com.example.blogsystemtest.controller.blog;

import com.example.blogsystemtest.annotation.SystemLog;
import com.example.blogsystemtest.domain.ResponseResult;
import com.example.blogsystemtest.mapper.ArticleMapper;
import com.example.blogsystemtest.service.ArticleService;
import com.example.blogsystemtest.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;


@RestController("blog-upload-controller")
public class UploadAndDownloadController {

    @Autowired
    private UploadService uploadService;

    @Autowired
    private ArticleMapper articleMapper;

    @PostMapping("/upload")
    @SystemLog(BusinessName = "上传图片")
    public ResponseResult uploadImg(@RequestBody MultipartFile img){
        return uploadService.uploadImg(img);
    }

    @PostMapping("/uploadFile")
    @SystemLog(BusinessName = "上传文件")
    public ResponseResult uploadFile(@RequestParam("file")MultipartFile file){
        return uploadService.uploadFile(file);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        String path=articleMapper.selectById(id).getFilePath();

        // 创建文件资源对象
        Resource fileResource = (Resource) new FileSystemResource(path);

        // 设置响应头部信息
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=file.txt");

        // 返回文件内容
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileResource);
    }
}
