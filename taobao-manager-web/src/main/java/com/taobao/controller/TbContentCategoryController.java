package com.taobao.controller;

import com.taobao.common.pojo.EasyUITreeNode;
import com.taobao.common.pojo.TaobaoResult;
import com.taobao.content.service.TbContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/content/category")
public class TbContentCategoryController {

    @Autowired
    private TbContentCategoryService tbContentCategoryService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public List<EasyUITreeNode> showTbContentCategoryTree(@RequestParam(value = "id",defaultValue = "0") Long id) {
        return tbContentCategoryService.showTbContentCategoryTree(id);
    }

//    $.post("/content/category/create",{parentId:node.parentId,name:node.text},function(data)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public TaobaoResult createTbContentCategory(Long parentId, String name) {
        return tbContentCategoryService.createTbContentCategory(parentId, name);
    }

//    $.post("/content/category/update",{id:node.id,name:node.text});
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public TaobaoResult updateTbContentCategory(Long id, String name) {
        return tbContentCategoryService.updateTbContentCategory(id, name);
    }

    //    $.post("/content/category/delete/",{id:node.id},function()
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public TaobaoResult deleteTbContentCategory(Long id) {
        return tbContentCategoryService.deleteTbContentCategory(id);
    }
}
