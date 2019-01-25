package com.taobao.service;

import com.taobao.common.pojo.EasyUIDataGridResult;
import com.taobao.pojo.TbItem;

public interface TbItemService {

    TbItem queryItemById(long id);

    EasyUIDataGridResult queryItemListWithPage(Integer page, Integer pageSize);
}
