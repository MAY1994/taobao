package com.taobao.service;

import com.taobao.common.pojo.EasyUITreeNode;
import com.taobao.pojo.TbItemCat;

import java.util.List;

public interface TbItemCatService {

    List<EasyUITreeNode> getItemList(Long parentId);

    TbItemCat queryItemCatById(Long itemId);

}
