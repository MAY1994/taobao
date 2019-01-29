package com.taobao.controller;

import com.taobao.common.pojo.EasyUITreeNode;
import com.taobao.service.TbItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class TbItemCatController {
    @Autowired
    TbItemCatService tbItemCatService;

    @RequestMapping("/item/cat/list")
    @ResponseBody
    public List<EasyUITreeNode> getItemList(@RequestParam(value = "id",defaultValue = "0") Long parentId) {
        return tbItemCatService.getItemList(parentId);
    }


}
