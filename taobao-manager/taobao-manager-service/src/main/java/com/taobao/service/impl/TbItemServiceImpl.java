package com.taobao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taobao.common.pojo.EasyUIDataGridResult;
import com.taobao.mapper.TbItemMapper;
import com.taobao.pojo.TbItem;
import com.taobao.pojo.TbItemExample;
import com.taobao.service.TbItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TbItemServiceImpl implements TbItemService {

    @Autowired
    TbItemMapper tbItemMapper;

    @Override
    public TbItem queryItemById(long id) {
        return tbItemMapper.selectByPrimaryKey(id);
    }

    @Override
    public EasyUIDataGridResult queryItemListWithPage(Integer page, Integer pageSize) {
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 30;
        }
        PageHelper.startPage(page,pageSize);
        TbItemExample example = new TbItemExample();
        List<TbItem> tbItems = tbItemMapper.selectByExample(example);
        PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(tbItems);
        EasyUIDataGridResult easyUIDataGridResult = new EasyUIDataGridResult();
        easyUIDataGridResult.setRows(pageInfo.getList());
        easyUIDataGridResult.setTotal((int) pageInfo.getTotal());
        return easyUIDataGridResult;
    }


}
