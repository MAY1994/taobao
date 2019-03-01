package com.taobao.cache.service;

import com.taobao.pojo.TbContent;
import com.taobao.pojo.TbItem;

public interface CacheService {
    /**
     * 将商品信息保存到本地缓存中
     * @param tbItem
     * @return
     */
    TbItem saveTbItemInfo2LocalCache(TbItem tbItem);

    /**
    TbItem saveTbItemInfo2LocalCache(TbItem tbItem);
     * 将商品内容信息保存到本地缓存中
     */
    TbContent saveTbContentInfo2LocalCache(TbContent tbContent);
    /**
     * 从本地缓存中获取商品信息
     * @param itemId
     * @return
     */
    TbItem getTbItemLocalCache(Long itemId);

    TbItem saveTbItemInfo2RedisCache(TbItem tbItem);

    TbContent saveTbContentInfo2RedisCache(TbContent tbContent);
}
