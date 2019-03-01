package com.taobao.content.service;

import com.taobao.common.pojo.EasyUIDataGridResult;
import com.taobao.common.pojo.TaobaoResult;
import com.taobao.pojo.TbContent;

import java.util.List;

public interface TbContentService {

    EasyUIDataGridResult queryTbContentList(Long categoryId, Integer page, Integer rows);

    TaobaoResult insertTbContent(TbContent tbContent);

    TaobaoResult updateTbContent(TbContent tbContent);

    TaobaoResult deleteTbContentById(Long... ids);

    List<TbContent> queryTbContentListByCatId(Long categoryId);

}
