package com.taobao.service;

import com.taobao.common.pojo.EasyUIDataGridResult;
import com.taobao.common.pojo.TaobaoResult;
import com.taobao.pojo.TbItem;
import com.taobao.pojo.TbItemDesc;

public interface TbItemService {

    TbItem queryItemById(long id);

    EasyUIDataGridResult queryItemListWithPage(Integer page, Integer pageSize);

    TaobaoResult createItem(TbItem tbItem, TbItemDesc tbItemDesc);

}
