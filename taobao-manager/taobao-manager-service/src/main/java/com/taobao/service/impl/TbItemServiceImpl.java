package com.taobao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taobao.common.pojo.EasyUIDataGridResult;
import com.taobao.common.pojo.TaobaoResult;
import com.taobao.common.utils.IDUtils;
import com.taobao.mapper.TbItemDescMapper;
import com.taobao.mapper.TbItemMapper;
import com.taobao.pojo.TbItem;
import com.taobao.pojo.TbItemDesc;
import com.taobao.pojo.TbItemExample;
import com.taobao.service.TbItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.print.attribute.standard.Destination;
import java.util.Date;
import java.util.List;

@Service
public class TbItemServiceImpl implements TbItemService {

    @Autowired
    TbItemMapper tbItemMapper;

    @Autowired
    TbItemDescMapper tbItemDescMapper;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Override
    public TbItem queryItemById(long id) {
        return tbItemMapper.selectByPrimaryKey(id);
    }

    @Override
    public TbItemDesc queryItemDescById(Long id) {
        return tbItemDescMapper.selectByPrimaryKey(id);
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

    @Override
    public TaobaoResult createItem(TbItem tbItem, TbItemDesc tbItemDesc) {
        //商品id
        final long itemId = IDUtils.genItemId();
        tbItem.setId(itemId);
        tbItemDesc.setItemId(itemId);
        //商品状态，1-正常，2-下架，3-删除
        tbItem.setStatus((byte) 1);
        //商品创建时间，更新时间
        Date updateDate = new Date();
        tbItem.setCreated(updateDate);
        tbItem.setUpdated(updateDate);
        tbItemDesc.setCreated(updateDate);
        tbItemDesc.setUpdated(updateDate);
        tbItemMapper.insert(tbItem);
        tbItemDescMapper.insert(tbItemDesc);

        /*将增加的item的信息添加到activemq中
        * 生产者
        * */
        javax.jms.Destination defaultDestination = jmsTemplate.getDefaultDestination();
        System.out.println("=========增加商品，生产消息===========");
        jmsTemplate.send(defaultDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                //格式为Json：{"serviceId":"tbItem","id":"155119146082640"}
                return session.createTextMessage("{\"serviceId\":\"tbItem\",\"id\":\""+tbItem.getId()+"\"}");
            }
        });

        return TaobaoResult.ok();
    }



}
