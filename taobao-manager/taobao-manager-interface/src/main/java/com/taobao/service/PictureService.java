package com.taobao.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface PictureService {

    Map pictureUpload(byte[] bytes, MultipartFile multiPartFile);

    Map now(String url);

}
