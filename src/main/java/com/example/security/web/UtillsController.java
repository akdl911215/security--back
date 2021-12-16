package com.example.security.web;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@RestController
public class UtillsController {

    private final ResourceLoader resourceLoader;

    @GetMapping("/image")
    public ResponseEntity<Resource> getImageWithMediaType() throws IOException {


        log.info("image 찾기");

        Resource resource = resourceLoader.getResource("classpath:/static/images/myimg.png");

        if(!resource.exists()){
            log.info("리소스를 찾을 수 없습니다.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Path path = Paths.get(resource.getURI());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", Files.probeContentType(path));
        return new ResponseEntity<>(resource,httpHeaders,HttpStatus.OK);
    }



    @GetMapping("/image2")
    public void getImageWithFileSystem(HttpServletResponse response) throws IOException {

        log.info("image 찾기");

        Resource resource = resourceLoader.getResource("file:C:\\Users\\songn\\Documents\\Lightshot\\Screenshot_6.png");
        if(!resource.exists()){
            log.info("리소스를 찾을 수 없습니다.");
            //throw
        }

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(resource.getInputStream(), response.getOutputStream());
    }


}
