package com.nowcoder.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nowcoder.controller.MessageController;
import com.nowcoder.model.User;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.*;

import java.util.logging.Logger;

/**
 * Created by Huangsky on 2018/8/14.
 */

@Service
public class JedisAdapter implements InitializingBean{

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(JedisAdapter.class);
    private JedisPool pool;


    public static void print(int index,Object obj){
        if (obj != null)
            System.out.println(String.format("%d,%s",index,obj.toString()));
        else
            System.out.println(String.format("%d,输出空指针了",index));
    }

    public static void main(String[] args){
       /* Jedis jedis = new Jedis("redis://localhost:6379/9");
        *//*jedis.flushDB();*//*

        *//*****key-value实例*****//*
        jedis.set("hello","world");
        print(1,jedis.get("hello"));
        jedis.rename("hello","newHello");
        print(1,jedis.get("newHello"));
        jedis.setex("helloEx",15,"worldEx");

        jedis.set("pv","100");
        jedis.incr("pv");
        jedis.incrBy("pv",5);
        print(2,jedis.get("pv"));
        jedis.decrBy("pv",2);
        print(2,jedis.get("pv"));

        print(3,jedis.keys("*"));


        *//*****List实例*****//*
        String listName = "list";
        jedis.del(listName);
        for (int i = 0;i<10;i++){
            jedis.lpush(listName,"a"+String .valueOf(i));
        }
        print(4,jedis.lrange(listName,0,12));
        print(4,jedis.lrange(listName,0,3));

        print(5,jedis.llen(listName));
        print(6,jedis.lpop(listName));
        print(7,jedis.llen(listName));
        print(8,jedis.lrange(listName,2,6));
        print(9,jedis.lindex(listName,3));
        print(10,jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER,"a4","xx"));
        print(10,jedis.linsert(listName, BinaryClient.LIST_POSITION.BEFORE,"a4","bb"));
        print(11,jedis.lrange(listName,0,12));


        *//*****Hash实例*****//*
        String userKey = "userXX";
        jedis.hset(userKey,"name","jim");
        jedis.hset(userKey,"age","12");
        jedis.hset(userKey,"phone","1174480720");
        print(12,jedis.hget(userKey,"name"));
        print(13,jedis.hgetAll(userKey));
        jedis.hdel(userKey,"phone");
        print(14,jedis.hgetAll(userKey));
        print(15,jedis.hexists(userKey,"email"));
        print(16,jedis.hexists(userKey,"age" ));
        print(17,jedis.hkeys(userKey));
        print(18,jedis.hvals(userKey));
        jedis.hsetnx(userKey,"school","SYSU");
        jedis.hsetnx(userKey,"name","yxy");
        print(14,jedis.hgetAll(userKey));

        *//*****List实例*****//*
        String likeKey1 = "commentLike1";
        String likeKey2 = "commentLike2";
        for (int i = 0;i < 10 ;i++){
            jedis.sadd(likeKey1,String.valueOf(i));
            jedis.sadd(likeKey2,String.valueOf(i*i));
        }
        print(20,jedis.smembers(likeKey1));
        print(21,jedis.smembers(likeKey2));
        print(22,jedis.sunion(likeKey1,likeKey2));
        print(23,jedis.sdiff(likeKey1,likeKey2));
        print(24,jedis.sinter(likeKey1,likeKey2));
        print(25,jedis.sismember(likeKey1,"13"));
        print(26,jedis.sismember(likeKey2,"16"));

        jedis.srem(likeKey1,"5");
        print(27,jedis.smembers(likeKey1));


        *//*从likeKey2从移动元素”25“至likeKey1 *//*
        jedis.smove(likeKey2,likeKey1,"25");
        print(28,jedis.smembers(likeKey1));
        print(29,jedis.smembers(likeKey2));

        String rankKey = "rankKey";
        jedis.zadd(rankKey,15,"jim" );
        jedis.zadd(rankKey,60,"Ben" );
        jedis.zadd(rankKey,90,"Lee" );
        jedis.zadd(rankKey,75,"Lucy" );
        jedis.zadd(rankKey,80,"Mei" );
        print(30,jedis.zcard(rankKey));//查询总人数
        print(31,jedis.zcount(rankKey,61,100));//查询区间在61-100的人数
        print(32,jedis.zscore(rankKey,"Lucy"));

        jedis.zincrby(rankKey,2,"Lucy");
        print(33,jedis.zscore(rankKey,"Lucy"));

        jedis.zincrby(rankKey,2,"Luc");//当用户不存在时，直接添加用户
        print(34,jedis.zscore(rankKey,"Luc"));

        print(35,jedis.zrange(rankKey,0,100));//选取前面100人，默认从分数低到高
        print(36,jedis.zrange(rankKey,0,10));

        print(37,jedis.zrevrange(rankKey,1,3));//选取前面100人，从分数高到低

        for (Tuple tuple : jedis.zrangeByScoreWithScores(rankKey,"60","100")){//选取分数区间在60-100之间的人，并输出姓名和分数
            print(38,tuple.getElement()+":"+String.valueOf(tuple.getScore()));
        }

        print(39,jedis.zrank(rankKey,"Ben"));//从小到大排序，处于第几个
        print(40,jedis.zrevrank(rankKey,"Ben"));//倒数第几个

        String setKey = "zset";
        jedis.zadd(setKey,1,"a");
        jedis.zadd(setKey,1,"b");
        jedis.zadd(setKey,1,"c");
        jedis.zadd(setKey,1,"d");
        jedis.zadd(setKey,1,"e");
        jedis.zadd(setKey,1,"f");

        print(41,jedis.zlexcount(setKey,"-","+"));//集合的边界[负无穷，正无穷]
        print(42,jedis.zlexcount(setKey,"[b","[d"));//集合的边界[b,d]之间的数量
        print(43,jedis.zlexcount(setKey,"(b","[d"));//集合的边界(b,d]之间的数量，不包括b
        jedis.zrem(setKey,"b");             //删除b
        print(44,jedis.zrange(setKey,0,10));

        jedis.zremrangeByLex(setKey,"(c","+");//把排在c右侧的元素全部删除，但是不包括c
        print(45,jedis.zrange(setKey,0,2));
*/

       /* print(46,jedis.get("pv"));*/

        //jedis的连接池
        JedisPool pool = new JedisPool();
        for (int i =0;i<100;i++){
            Jedis j = pool.getResource();
            print(46,j.get("pv"));
            j.close();
        }

        //使用redis做缓存
        /*User user = new User();
        user.setName("xx");
        user.setPassword("123");
        user.setHeadUrl("aaa.png");
        user.setSalt("salt");
        user.setId(1);
        print(47, JSONObject.toJSONString(user));
        jedis.set("user1",JSONObject.toJSONString(user));//序列化

        String value = jedis.get("user1");
        User user2 = JSON.parseObject(value,User.class);//反序列化
        print(48,user2);*/
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("redis//localhost:6379/10");
    }

    //增加
    public long sadd (String key,String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key,value);
        }catch (Exception e){
            logger.error("发生异常："+e.getMessage());
        }finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    //删除
    public long srem (String key,String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key,value);
        }catch (Exception e){
            logger.error("发生异常："+e.getMessage());
        }finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    //统计
    public long scard(String key){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        }catch (Exception e){
            logger.error("发生异常："+e.getMessage());
        }finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    //统计
    public boolean sismember(String key,String value){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key,value);
        }catch (Exception e){
            logger.error("发生异常："+e.getMessage());
        }finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return false;
    }


}
