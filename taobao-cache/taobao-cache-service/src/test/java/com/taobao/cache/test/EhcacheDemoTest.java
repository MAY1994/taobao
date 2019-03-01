package com.taobao.cache.test;

import com.taobao.cache.service.CacheService;
import com.taobao.pojo.TbItem;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext-*.xml")
public class EhcacheDemoTest {

    @Autowired
    CacheService ehCacheService;

    @Test
    public void test() {
        CacheManager cacheManager = new CacheManager();
        Cache cache = cacheManager.getCache("helloworld");
        System.out.println(cache);
        String key = "greeting";
        Element putElement = new Element(key, "hello,world");
        cache.put(putElement);
        Element getElement = cache.get(key);
        System.out.println(getElement);
    }

    @Test
    public void test02() {
        TbItem tbItem = new TbItem();
        tbItem.setId(100L);
        tbItem.setUpdated(new Date());
        tbItem.setNum(100);
        ehCacheService.saveTbItemInfo2LocalCache(tbItem);
        TbItem localCache = ehCacheService.getTbItemLocalCache(100L);
        System.out.println(localCache);
    }

}
