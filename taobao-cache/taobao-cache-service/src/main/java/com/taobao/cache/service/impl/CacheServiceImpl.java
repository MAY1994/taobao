package com.taobao.cache.service.impl;

import com.taobao.cache.jedis.JedisClient;
import com.taobao.cache.service.CacheService;
import com.taobao.common.utils.JsonUtils;
import com.taobao.pojo.TbContent;
import com.taobao.pojo.TbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CacheServiceImpl implements CacheService {

    private static final String CACHE_NAME = "helloworld";

    @Autowired
    private JedisClient jedisClient;

    @Override
    @CachePut(value = CACHE_NAME, key = "'tbItem_key_'+#tbItem.getId()")
    public TbItem saveTbItemInfo2LocalCache(TbItem tbItem) {
        System.out.println("save方法的itemId="+tbItem.getId());
        return tbItem;
    }

    @Override
    @CachePut(value = CACHE_NAME, key = "'tbContent_key_'+#tbContent.getId()")
    public TbContent saveTbContentInfo2LocalCache(TbContent tbContent) {
        System.out.println("save方法的itemId="+tbContent.getId());
        return tbContent;
    }

    @Override
    @Cacheable(value = CACHE_NAME, key = "'tbItem_key_'+#itemId")
    public TbItem getTbItemLocalCache(Long itemId) {
        System.out.println("get方法的itemId="+itemId);
        return null;
    }

    @Override
    public TbItem saveTbItemInfo2RedisCache(TbItem tbItem) {
        String key = "tbItem_info" + tbItem.getId();
        jedisClient.set(key, JsonUtils.objectToJson(tbItem));
        return tbItem;
    }

    @Override
    public TbContent saveTbContentInfo2RedisCache(TbContent tbContent) {
        String key = "tbContent_info" + tbContent.getId();
        jedisClient.set(key, JsonUtils.objectToJson(tbContent));
        return tbContent;
    }
}
