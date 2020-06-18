package com.usian;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.domain.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.fileupload.FileUpload;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ItemWebApp.class})
public class FastDFSTest {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Autowired
    private ThumbImageConfig imageConfig;

    @Test
    public void testUpload() throws FileNotFoundException {
        File file = new File("d:/1.jpg");
        StorePath storePath = fastFileStorageClient.uploadFile(
                new FileInputStream(file), file.length(), "jpg", null
        );
        System.out.println(storePath.getFullPath());
        System.out.println(storePath.getPath());
    }

    @Test
    public void testUploadAndCreateThumb() throws FileNotFoundException {
        File file = new File("d:/1.jpg");
        StorePath storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(
                new FileInputStream(file), file.length(), "jpng", null
        );
        System.out.println(storePath.getFullPath());
        System.out.println(storePath.getPath());
        String path = imageConfig.getThumbImagePath(storePath.getFullPath());
        System.out.println(path);
    }
}
