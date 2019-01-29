package com.taobao.service;

import com.taobao.common.pojo.EasyUITreeNode;

import java.util.List;

public interface TbItemCatService {

    List<EasyUITreeNode> getItemList(Long parentId);


}
