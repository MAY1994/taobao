package com.taobao.service.impl;

import com.taobao.common.utils.FtpUtil;
import com.taobao.common.utils.IDUtils;
import com.taobao.mapper.TestMapper;
import com.taobao.service.PictureService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PictureServiceImpl implements PictureService {
    @Value("${ftp.host}")
    private String FTP_HOST;

    @Value("${ftp.port}")
    private Integer FTP_PORT;

    @Value("${ftp.username}")
    private String FTP_USERNAME;

    @Value("${ftp.password}")
    private String FTP_PASSWORD;

    @Value("${ftp.basePath}")
    private String FTP_BASE_PATH;

    @Value("${ftp.accessPath}")
    private String FTP_ACCESS_PATH;

    @Override
    public Map pictureUpload(byte[] bytes, MultipartFile multipartFile) {
        Map<String, Object> map = new HashMap<>();
        //获得原始图片名
        String originalFilename = multipartFile.getOriginalFilename();
        //获得原始图片后缀名
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //创建新图片名
//        String newFullFilename = String.valueOf(UUID.randomUUID()) + suffix;
        String newFullFilename = IDUtils.genImageName() + suffix;
        //创建filePath
        String newFilePath = new DateTime().toString("/yyyy/MM/dd/");
        try {
            //上传文件
            boolean result = FtpUtil.uploadFile(FTP_HOST, FTP_PORT, FTP_USERNAME, FTP_PASSWORD,
                    FTP_BASE_PATH, newFilePath, newFullFilename, new ByteArrayInputStream(bytes));
            //返回前端结果
            if (!result) {
//                PictureResult pictureResult = new PictureResult(1, null);
//                pictureResult.setMessage("文件上传失败");
//                return pictureResult;
                map.put("error", 1);
                map.put("message", "文件上传失败");
                return map;
            } else {
//                return new PictureResult(0, FTP_BASE_PATH+newFilePath+newFullFilename,
//                        "文件上传成功！");
                map.put("error", 0);
                map.put("message", "文件上传成功");
                map.put("url", "http://"+FTP_HOST+FTP_ACCESS_PATH + newFilePath + newFullFilename);
                return map;
            }
        } catch (Exception e) {
            e.printStackTrace();
//            PictureResult pictureResult = new PictureResult(1, null);
//            pictureResult.setMessage("文件上传异常");
//            return pictureResult;
            map.put("error", 1);
            map.put("message", "文件上传异常");
            return map;
        }
    }

    @Override
    public Map now(String url) {
        String hello = testMapper.hello();
        Map<String, String> map = new HashMap<>();
        map.put("now", hello+url);
        return map;
    }

    @Autowired
    TestMapper testMapper;

}
