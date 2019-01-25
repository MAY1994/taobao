package com.taobao.service.impl;

import com.taobao.pojo.TbItem;
import com.taobao.service.TbItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-*.xml")
public class TbItemServiceImplTest {

    @Autowired
    TbItemService tbItemService;

    @Test
    public void queryItemById() {
        TbItem tbItem = tbItemService.queryItemById(536563);
        System.out.println(tbItem.getBarcode());
        System.out.println(tbItem.getId());
        System.out.println(tbItem.getTitle());

    }
}