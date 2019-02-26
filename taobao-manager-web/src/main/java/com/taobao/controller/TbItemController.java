package com.taobao.controller;

import com.taobao.common.pojo.EasyUIDataGridResult;
import com.taobao.common.pojo.TaobaoResult;
import com.taobao.content.service.TbContentService;
import com.taobao.pojo.TbItem;
import com.taobao.pojo.TbItemCat;
import com.taobao.pojo.TbItemDesc;
import com.taobao.service.TbItemCatService;
import com.taobao.service.TbItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TbItemController {

    @Autowired
    TbItemService tbItemService;

    @Autowired
    TbItemCatService tbItemCatService;

    @Autowired
    TbContentService tbContentService;

    @RequestMapping("/item/{id}")
    @ResponseBody
    public TbItem queryItemById(@PathVariable("id") long id) {
        return tbItemService.queryItemById(id);
    }

    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUIDataGridResult list(@RequestParam("page")Integer page, @RequestParam("rows")Integer rows) {
        EasyUIDataGridResult easyUIDataGridResult = tbItemService.queryItemListWithPage(page, rows);
        return easyUIDataGridResult;
    }

    @RequestMapping("/item/save")
    @ResponseBody
    public TaobaoResult createItem(TbItem tbItem, @RequestParam("desc") String itemDesc) {
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemDesc(itemDesc);
        TaobaoResult result = tbItemService.createItem(tbItem,tbItemDesc);
        return result;
    }

    @RequestMapping("/rest/page/item-edit")
    public String editItem(@RequestParam("_") Long itemId, Model model) {
        TbItem tbItem = tbItemService.queryItemById(itemId);
        TbItemCat tbItemCat = tbItemCatService.queryItemCatById(itemId);
        String Category = tbItemCat.getName();
//        tbContentService.queryContentById(itemId);

        model.addAttribute("tbItem",tbItem);
        model.addAttribute("Category", Category);
        return "item-edit";
    }

}
