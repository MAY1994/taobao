package com.taobao.controller;

import com.taobao.service.PictureService;
import org.apache.commons.fileupload.MultipartStream;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Controller
public class PictureController {
    @Autowired
    PictureService pictureService;

    @RequestMapping(path = "/pic/upload")
    @ResponseBody
    public Map pictureUpload(MultipartFile uploadFile) throws IOException {
//        PictureResult pictureResult = pictureService.pictureUpload(uploadFile);
//        return JsonUtils.objectToJson(pictureResult);
        byte[] bytes = uploadFile.getBytes();
        Map map = pictureService.pictureUpload(bytes,uploadFile);
        return map;
    }

    @RequestMapping(path = "/pic/now")
    @ResponseBody
    public Map now(@RequestParam("url") String url) {
//        PictureResult pictureResult = pictureService.pictureUpload(uploadFile);
//        return JsonUtils.objectToJson(pictureResult);
        Map now = pictureService.now(url);
        return now;
    }


}
