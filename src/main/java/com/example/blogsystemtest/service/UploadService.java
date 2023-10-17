package com.example.blogsystemtest.service;


import com.example.blogsystemtest.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


public interface UploadService {
    ResponseResult uploadImg(MultipartFile img);
    ResponseResult uploadFile(MultipartFile file);

    void download(String path, HttpServletResponse response);

}
