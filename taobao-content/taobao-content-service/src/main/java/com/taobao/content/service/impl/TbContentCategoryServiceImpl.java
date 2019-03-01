package com.taobao.content.service.impl;

import com.taobao.common.pojo.EasyUITreeNode;
import com.taobao.common.pojo.TaobaoResult;
import com.taobao.content.service.TbContentCategoryService;
import com.taobao.mapper.TbContentCategoryMapper;
import com.taobao.pojo.TbContentCategory;
import com.taobao.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TbContentCategoryServiceImpl implements TbContentCategoryService {

    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;

    @Override
    public List<EasyUITreeNode> showTbContentCategoryTree(long parentId) {
        TbContentCategoryExample example = new TbContentCategoryExample();
        // status 状态。可选值:1(正常),2(删除)
        example.createCriteria().andParentIdEqualTo(parentId).andStatusEqualTo(1);
        List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);
        List<EasyUITreeNode> nodes = new ArrayList<>();
        for (TbContentCategory contentCategory : list) {
            EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
            easyUITreeNode.setId(contentCategory.getId());
            easyUITreeNode.setState(contentCategory.getIsParent()?"closed":"open");
            easyUITreeNode.setText(contentCategory.getName());
            nodes.add(easyUITreeNode);
        }
        return nodes;
    }

    @Override
    public TaobaoResult createTbContentCategory(Long parentId, String name) {
        TbContentCategory tbContentCategory = new TbContentCategory();
        tbContentCategory.setParentId(parentId);
        tbContentCategory.setName(name);
//        该类目是否为父类目，1为true，0为false
        tbContentCategory.setIsParent(false);
        tbContentCategory.setSortOrder(1);
//        状态。可选值:1(正常),2(删除)
        tbContentCategory.setStatus(1);
        tbContentCategory.setCreated(new Date());
        tbContentCategory.setUpdated(tbContentCategory.getCreated());
        //插入一个内容分类
        tbContentCategoryMapper.insertSelective(tbContentCategory);
        //更新父节点的isParent属性
        TbContentCategory parentNode = new TbContentCategory();
        parentNode.setId(parentId);
        parentNode.setIsParent(true);
        tbContentCategoryMapper.updateByPrimaryKeySelective(parentNode);
        return TaobaoResult.ok(tbContentCategory);
    }

    @Override
    public TaobaoResult updateTbContentCategory(Long id, String name) {
        TbContentCategory tbContentCategory = new TbContentCategory();
        tbContentCategory.setId(id);
        tbContentCategory.setName(name);
        tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);
        return TaobaoResult.ok(tbContentCategory);
    }

    @Override
    public TaobaoResult deleteTbContentCategory(Long id) {
        if (id == null) {
            return TaobaoResult.build(404,"ERROR");
        }
        TbContentCategory node = tbContentCategoryMapper.selectByPrimaryKey(id);
        //该节点是否为父亲节点，若是，则删除失败，否则更新该节点的status为2，表示删除
        if (node.getIsParent()) {
            return TaobaoResult.build(404,"ERROR");
        }
        //status NULL状态。可选值:1(正常),2(删除)
        node.setStatus(2);
        tbContentCategoryMapper.updateByPrimaryKeySelective(node);
        //更新父节点的isParent状态
        Long parentId = node.getParentId();
        TbContentCategoryExample example = new TbContentCategoryExample();
        //找到该节点的父节点的所有“未删除”的子节点
        example.createCriteria().andParentIdEqualTo(parentId).andStatusEqualTo(1);
        List<TbContentCategory> nodes = tbContentCategoryMapper.selectByExample(example);
        //找到这个节点的父节点，如果父节点没有子节点了则将父节点变为子节点
        if (nodes.size() == 0) {
            TbContentCategory parentNode = new TbContentCategory();
            parentNode.setId(parentId);
            parentNode.setIsParent(false);
            tbContentCategoryMapper.updateByPrimaryKeySelective(parentNode);
        }
        return TaobaoResult.ok(node);
    }
}
