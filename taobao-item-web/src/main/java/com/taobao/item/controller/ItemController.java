package com.taobao.item.controller;

import com.taobao.item.pojo.Item;
import com.taobao.pojo.TbItem;
import com.taobao.pojo.TbItemDesc;
import com.taobao.service.TbItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ItemController {

    @Autowired
    private TbItemService tbItemService;


    @RequestMapping("/item/{itemId}")
    public String getItem(@PathVariable Long itemId, Model model){
        //1.引入服务
        //2.注入服务
        //3.调用服务的方法
        //商品的基本信息   tbitem  没有getImages
        TbItem tbItem = tbItemService.queryItemById(itemId);
        //商品的描述信息
        TbItemDesc itemDesc = tbItemService.queryItemDescById(itemId);
        //4.tbitem 转成 item
        TbItem item = new Item(tbItem);
        //5.传递数据到页面中
        model.addAttribute("item", item);
        model.addAttribute("itemDesc", itemDesc);
        return "item";
    }

}
