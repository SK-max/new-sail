package com.usian.controller;


import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.usian.Result;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 图片上传
 */
@RestController
@RequestMapping("/file")
public class FileUploadController {

    @Autowired
    private FastFileStorageClient storageClient;

    private static final List<String> CONTENT_TYPES= Arrays.asList("image/jpg","image/gif");


    @RequestMapping("/upload")
    public Result fileUpload(MultipartFile file){
        try {
            String filename = file.getOriginalFilename();
            //检验文件的类型
            String contentType = file.getContentType();
            if(CONTENT_TYPES.contains(contentType)){
                //文件内容不合法，直接返回
                return Result.error("文件类型不合法:"+filename);
            }
            //校验文件的内容
            BufferedImage read = ImageIO.read(file.getInputStream());
            if(read==null){
                return Result.error("文件类型不合法:"+filename);
            }
            String lastName = StringUtils.substringAfterLast(filename, ".");
            StorePath path = storageClient.uploadFile(file.getInputStream(), file.getSize(), lastName, null);
            System.out.println(path.getFullPath());
            //生成URL返回
            return Result.ok("http://image.usian.com/"+path.getFullPath());
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.error("服务器内部错误");
    }

}
