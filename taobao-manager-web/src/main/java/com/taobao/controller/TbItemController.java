package com.taobao.controller;

import com.taobao.common.pojo.EasyUIDataGridResult;
import com.taobao.pojo.TbItem;
import com.taobao.service.TbItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/item")
@Controller
public class TbItemController {

    @Autowired
    TbItemService tbItemService;

    @RequestMapping("/{id}")
    @ResponseBody
    public TbItem queryItemById(@PathVariable("id") long id) {
        return tbItemService.queryItemById(id);
    }

    @RequestMapping("/list")
    @ResponseBody
    public EasyUIDataGridResult list(@RequestParam("page")Integer page, @RequestParam("rows")Integer rows) {
        EasyUIDataGridResult easyUIDataGridResult = tbItemService.queryItemListWithPage(page, rows);
        return easyUIDataGridResult;
    }

}
