package com.taobao.service.impl;

import com.taobao.common.pojo.EasyUITreeNode;
import com.taobao.mapper.TbItemCatMapper;
import com.taobao.pojo.TbItemCat;
import com.taobao.pojo.TbItemCatExample;
import com.taobao.service.TbItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TbItemCatServiceImpl implements TbItemCatService {
    @Autowired
    TbItemCatMapper tbItemCatMapper;

    @Override
    public List<EasyUITreeNode> getItemList(Long parentId) {
        TbItemCatExample example = new TbItemCatExample();
        example.createCriteria().andParentIdEqualTo(parentId);
        List<TbItemCat> tbItemCats = tbItemCatMapper.selectByExample(example);
        List<EasyUITreeNode> list = new ArrayList<>();
        for (TbItemCat tbItemCat : tbItemCats) {
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(tbItemCat.getId());
            node.setText(tbItemCat.getName());
            node.setState(tbItemCat.getIsParent() ? "closed" : "open");
            list.add(node);
        }
        return list;
    }
}
