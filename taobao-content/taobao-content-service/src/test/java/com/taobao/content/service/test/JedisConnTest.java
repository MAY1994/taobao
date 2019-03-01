package com.taobao.content.service.test;

import com.taobao.content.jedis.JedisClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext-redis.xml")
public class JedisConnTest {

    @Autowired
    JedisClient jedisClient;

    @Test
    public void testJedis() {
        jedisClient.set("JedisClientKey11", "JedisClientKey11");
        System.out.println(jedisClient.get("JedisClientKey11"));
    }

    @Test
    public void testJedisCluster() {
        jedisClient.set("JedisClusterKey22", "JedisClusterKey33");
        System.out.println(jedisClient.get("JedisClusterKey22"));
    }
}
