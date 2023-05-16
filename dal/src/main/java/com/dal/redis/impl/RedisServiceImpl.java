package com.dal.redis.impl;

import com.dal.redis.RedisService;
import com.common.utils.LogFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2022/2/9 2:37 下午
 * description：
 */
@Component
public class RedisServiceImpl implements RedisService {
    
    @Autowired
    private JedisPool jedisPool;

    private static final Logger REDIS_SYNC_LOG = LogFactory.REDIS_LOGGER;

    @Override
    public boolean set(String key, String value){
        try(Jedis jedis = jedisPool.getResource()){
            String result = jedis.set(key, value);
            REDIS_SYNC_LOG.info("[RedisServiceImpl#set] key={}, value={}, result={}", key, value, result);
            return "OK".equalsIgnoreCase(result);
        }
    }

    @Override
    public String get(String key) {
        try(Jedis jedis = jedisPool.getResource()){
            String result = jedis.get(key);
            REDIS_SYNC_LOG.info("[RedisServiceImpl#get] key={}, result={}", key, result);
            return result;
        }
    }

    @Override
    public long expire(String key, long seconds) {
        try(Jedis jedis = jedisPool.getResource()){
            long result = jedis.expire(key, seconds);
            REDIS_SYNC_LOG.info("[RedisServiceImpl#expire] key={} seconds={}, result={}", key, seconds, result);
            return result;
        }
    }

    @Override
    public long incr(String key) {
        try(Jedis jedis = jedisPool.getResource()){
            long result = jedis.incr(key);
            REDIS_SYNC_LOG.info("[RedisServiceImpl#incr] key={}, result={}", key, result);
            return result;
        }
    }

}
