package com.taobao.portal.controller;

import com.taobao.common.utils.JsonUtils;
import com.taobao.content.service.TbContentService;
import com.taobao.pojo.TbContent;
import com.taobao.portal.pojo.Ad1Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PageController {

    @Value("${AD1_CATEFGORY_ID}")
    private Long AD1_CATEFGORY_ID;

    @Value("${AD1_PICTURE_HEIGHT}")
    private String AD1_PICTURE_HEIGHT;

    @Value("${AD1_PICTURE_HEIGHT_B}")
    private String AD1_PICTURE_HEIGHT_B;

    @Value("${AD1_PICTURE_WIDTH}")
    private String AD1_PICTURE_WIDTH;

    @Value("${AD1_PICTURE_WIDTH_B}")
    private String AD1_PICTURE_WIDTH_B;

    @Autowired
    private TbContentService tbContentService;

    @RequestMapping("/index.html")
    public String showIndex(Model model) {
        //先将轮播图等信息查出来
        List<TbContent> ad1Contents = tbContentService.queryTbContentListByCatId(AD1_CATEFGORY_ID);
        //不能直接将tbContents转为json后加到model中，因为所需的json中的属性与tbContent有所差别：
        /*	tbContent:
            private Long id;
            private Long categoryId;
            private String title;
            private String subTitle;
            private String titleDesc;
            private String url;
            private String pic;
            private String pic2;
            private Date created;
            private Date updated;
            private String content;
            ============>
            "srcB": "http://192.168.182.129/images/2019/02/16/1550298403737938.jpg",
            "height": 240,
            "alt": "",
            "width": 670,
            "src": "http://192.168.182.129/images/2019/02/16/1550298403737938.jpg",
            "widthB": 550,
            "href": "http://sale.jd.com/act/e0FMkuDhJz35CNt.html?cpdad=1DLSUE",
            "heightB": 240
            */
        List<Ad1Node> ad1Nodes = new ArrayList<>();
        for (TbContent tbContent : ad1Contents) {
            Ad1Node ad1Node = new Ad1Node();
            ad1Node.setAlt(tbContent.getSubTitle());
            ad1Node.setHref(tbContent.getUrl());
            ad1Node.setSrc(tbContent.getPic());
            ad1Node.setSrcB(tbContent.getPic2());
            ad1Node.setHeight(AD1_PICTURE_HEIGHT);
            ad1Node.setHeightB(AD1_PICTURE_HEIGHT_B);
            ad1Node.setWidth(AD1_PICTURE_WIDTH);
            ad1Node.setWidthB(AD1_PICTURE_WIDTH_B);
            ad1Nodes.add(ad1Node);
        }
        String ad1ContentsJsonArr = JsonUtils.objectToJson(ad1Nodes);
        model.addAttribute("ad1", ad1ContentsJsonArr);

        return "index";
    }


}
