package com.taobao.content.service;

import com.taobao.common.pojo.EasyUITreeNode;
import com.taobao.common.pojo.TaobaoResult;

import java.util.List;

public interface TbContentCategoryService {

    List<EasyUITreeNode> showTbContentCategoryTree(long parentId);

    TaobaoResult createTbContentCategory(Long parentId, String name);

    TaobaoResult updateTbContentCategory(Long id, String name);

    TaobaoResult deleteTbContentCategory(Long id);
}
