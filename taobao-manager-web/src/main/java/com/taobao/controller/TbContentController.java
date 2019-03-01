package com.taobao.controller;

import com.taobao.common.pojo.EasyUIDataGridResult;
import com.taobao.common.pojo.TaobaoResult;
import com.taobao.content.service.TbContentService;
import com.taobao.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TbContentController {

    @Autowired
    private TbContentService tbContentService;

    @RequestMapping(value = "/content/query/list", method = RequestMethod.GET)
    @ResponseBody
    public EasyUIDataGridResult showTbContentList(Long categoryId, Integer page, Integer rows) {
        EasyUIDataGridResult easyUIDataGridResult = tbContentService.queryTbContentList(categoryId, page, rows);
        return easyUIDataGridResult;
    }

    @RequestMapping(value = "/content/save", method = RequestMethod.POST)
    @ResponseBody
    public TaobaoResult saveTbContent(TbContent tbContent) {
        return tbContentService.insertTbContent(tbContent);
    }

    //    rest/content/edit
    @RequestMapping(value = "/rest/content/edit", method = RequestMethod.POST)
    @ResponseBody
    public TaobaoResult updateTbContent(TbContent tbContent) {
        return tbContentService.updateTbContent(tbContent);
    }

    //    /content/delete
    @RequestMapping(value = "/content/delete", method = RequestMethod.POST)
    @ResponseBody
    public TaobaoResult deleteTbContent(Long[] ids) {
        return tbContentService.deleteTbContentById(ids);
    }

}
