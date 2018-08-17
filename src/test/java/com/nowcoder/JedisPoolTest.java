package com.nowcoder;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by Huangsky on 2018/8/17.
 */

public class JedisPoolTest {
    public static void main(String[] args) {
        // 构建连接池配置对象
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 设置最大连接数
        jedisPoolConfig.setMaxTotal(50);

        // 构建连接池
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, "127.0.0.1", 6379);

        // 从连接池中获取连接
        Jedis jedis = jedisPool.getResource();

        // 读取数据
        System.out.println(jedis.get("mytest"));

        // 将连接还回到连接池中
        jedis.close();

        // 释放连接
        jedisPool.close();
    }
}
