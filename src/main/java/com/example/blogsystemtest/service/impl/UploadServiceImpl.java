package com.example.blogsystemtest.service.impl;



import com.example.blogsystemtest.domain.ResponseResult;
import com.example.blogsystemtest.enums.AppHttpCodeEnum;
import com.example.blogsystemtest.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;


@Service
@Slf4j
public class UploadServiceImpl implements UploadService {

    @Value("${file.upload.path}")
    private String fileSavePath;

    @Value("${file.coming.path}")
    private String fileComingPath;

    @Override
    public ResponseResult uploadImg(MultipartFile upload) {
        try {

            //获取文件上传路径
            String path = fileSavePath;
            log.info("文件上传路径为："+path);
            File file = new File(path);
            //判断不存在该目录就创建
            if (!file.exists()){
                file.mkdirs();
            }
            String contentType=upload.getContentType();
            if(contentType == null && (!contentType.equals("image/png") || !contentType.equals("image/jpeg"))){
                return ResponseResult.errorResult(AppHttpCodeEnum.FILE_TYPE_ERROR);

            }
            BufferedImage image = ImageIO.read(upload.getInputStream());
            if(image==null){
                return ResponseResult.errorResult(AppHttpCodeEnum.FILE_INVALID);
            }
            //获取文件名
            String filename = upload.getOriginalFilename();
            log.info(filename);
            //起别名
            String s = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            filename=s+filename;
            //开始上传
            upload.transferTo(new File(file,filename));
//            String nginxStoragePath = "/path/to/nginx/storage/directory/";
//            String finalFilePath = nginxStoragePath + originalFilename;
//            Files.move(Paths.get(tempFilePath), Paths.get(finalFilePath));
            String comingPath=fileComingPath+filename;

            log.info("最终文件的访问路径："+fileComingPath+filename);

            return ResponseResult.okResult(comingPath);
        } catch (IOException e) {
            log.error("图片上传失败"+upload.getOriginalFilename());
            throw new RuntimeException("图片上传失败");
        }
    }

    @Override
    public ResponseResult uploadFile(MultipartFile upload) {
        try {

            //获取文件上传路径
            String path = fileSavePath;
            log.info("文件上传路径为："+path);
            File file = new File(path);
            //判断不存在该目录就创建
            if (!file.exists()){
                file.mkdirs();
            }
            String originalFilename = upload.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
            if(fileExtension==null&&(fileExtension.equals("pdf")||fileExtension.equals("doc")||fileExtension.equals("xlsx"))){
                return ResponseResult.errorResult(AppHttpCodeEnum.FILE_TYPE_ERROR);
            }
            //获取文件名
            String filename = upload.getOriginalFilename();
            log.info(filename);
            //起别名
            String s = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            filename=s+filename;
            //开始上传
            upload.transferTo(new File(file,filename));
            return ResponseResult.okResult(fileSavePath+filename);
        } catch (IOException e) {
            log.error("文件上传失败"+upload.getOriginalFilename());
            throw new RuntimeException("文件上传失败");
        }
    }

    @Override
    public void download(String path, HttpServletResponse response) {
        try {
            // path是指想要下载的文件的路径
            File file = new File(path);
            log.info(file.getPath());
            // 获取文件名
            String filename = file.getName();
            // 获取文件后缀名
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
            log.info("文件后缀名：" + ext);

            // 将文件写入输入流
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStream fis = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();

            // 清空response
            response.reset();
            // 设置response的Header
            response.setCharacterEncoding("UTF-8");
            //Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
            //attachment表示以附件方式下载   inline表示在线打开   "Content-Disposition: inline; filename=文件名.mp3"
            // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            // 告知浏览器文件的大小
            response.addHeader("Content-Length", "" + file.length());
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            outputStream.write(buffer);
            outputStream.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


}
