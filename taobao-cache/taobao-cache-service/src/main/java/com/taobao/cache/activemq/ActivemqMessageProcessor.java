package com.taobao.cache.activemq;

import com.alibaba.fastjson.JSONObject;
import com.taobao.cache.service.CacheService;
import com.taobao.mapper.TbItemMapper;
import com.taobao.pojo.TbContent;
import com.taobao.pojo.TbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.jms.JMSException;
import javax.jms.TextMessage;

@Repository
public class ActivemqMessageProcessor implements Runnable {

    @Autowired
    public CacheService cacheService;

    @Autowired
    TbItemMapper tbItemMapper;

    private TextMessage message;

    public void setMessage(TextMessage message) {
        this.message = message;
    }

    public ActivemqMessageProcessor() {}

    @Override
    public void run() {

        System.out.println("进入Activemq消息处理程序");
        //转成Json串
        try {
            String text = message.getText();
            System.out.println(text);
            JSONObject messageJSONObject = JSONObject.parseObject(text);
            String serviceId = messageJSONObject.getString("serviceId");
            System.out.println(serviceId);
            //如果是商品信息服务
            if ("tbItem".equals(serviceId)) {
                System.out.println("调用商品信息缓存服务");
                processTbItemInfoChangeMessage(messageJSONObject);
            } else if ("tbContent".equals(serviceId)) {
                //如果是商品内容服务
                System.out.println("调用商品内容缓存服务");

                processTbContentInfoChangeMessage(messageJSONObject);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

    private void processTbItemInfoChangeMessage(JSONObject messageJSONObject) {
        Long id = null;
        id = messageJSONObject.getLong("id");
        if (id == null || -1 == id) return;
        //从数据库查询该id对应的信息
//        String tbItemInfoJSON = "{\"id\": 1, \"name\": \"iphone7手机\", \"price\": 5599, \"pictureList\":\"a.jpg,b.jpg\", \"specification\": \"iphone7的规格\", \"service\": \"iphone7的售后服务\", \"color\": \"红色,白色,黑色\", \"size\": \"5.5\", \"shopId\": 1}";
        /*
        * 这个商品信息应该去mysql数据库查呢，还是应该直接从消息队列传过来？
        * */
        TbItem tbItemInfo = tbItemMapper.selectByPrimaryKey(id);

        //将数据存入本地缓存
//        TbItem tbItem = JSONObject.parseObject(tbItemInfoJSON,TbItem.class);
        cacheService.saveTbItemInfo2LocalCache(tbItemInfo);
        cacheService.saveTbItemInfo2RedisCache(tbItemInfo);

        System.out.println("刚缓存到本地ehcache的缓存为："+cacheService.getTbItemLocalCache(id));
    }

    private void processTbContentInfoChangeMessage(JSONObject messageJSONObject) {
        long id = Long.parseLong(messageJSONObject.getString("id"));
        if (-1 == id) return;
        //从数据库查询该id对应的信息
        //todo
        String tbContentInfoJSON = "\"id\": 1, \"desc\": \"张三小店的东西很不错\"";

        TbContent tbContent = JSONObject.parseObject(tbContentInfoJSON,TbContent.class);
        //将数据存入本地缓存
        cacheService.saveTbContentInfo2LocalCache(tbContent);
        cacheService.saveTbContentInfo2RedisCache(tbContent);
        System.out.println("刚缓存到本地和redis的缓存为："+tbContent);
    }



}
