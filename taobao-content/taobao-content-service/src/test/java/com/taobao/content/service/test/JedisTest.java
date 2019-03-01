package com.taobao.content.service.test;

import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Set;

public class JedisTest {

    /*单机版测试*/
    @Test
    public void test() {
        Jedis jedis = new Jedis("192.168.182.133", 6379);
        jedis.set("k1", "v1");
        System.out.println(jedis.get("k1"));
        jedis.close();
    }

    /*单机版连接池测试*/
    @Test
    public void testJedisPool() {
        JedisPool jedisPool = new JedisPool("192.168.182.133", 6379);
        Jedis jedis = jedisPool.getResource();
        jedis.set("jedisPollKey", "jedisPollValue");
        System.out.println(jedis.get("jedisPollKey"));
        jedis.close();
        jedisPool.close();
    }

    /*JedisCluster 测试*/
    @Test
    public void testJedisCluster() {
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.182.133", 7001));
        nodes.add(new HostAndPort("192.168.182.133", 7002));
        nodes.add(new HostAndPort("192.168.182.134", 7003));
        nodes.add(new HostAndPort("192.168.182.134", 7004));
        nodes.add(new HostAndPort("192.168.182.135", 7005));
        nodes.add(new HostAndPort("192.168.182.135", 7006));
        JedisCluster jedisCluster = new JedisCluster(nodes);
        String set = jedisCluster.set("jedisClusterKey", "jedisClusterValue");
        System.out.println(set);
        System.out.println(jedisCluster.get("jedisClusterKey"));
        jedisCluster.close();
    }

}
