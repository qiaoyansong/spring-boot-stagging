package com.dal.redis;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2022/2/9 2:36 下午
 * description：
 */
public interface RedisService {

    /**
     * 设置缓存
     * @param key
     * @param value
     * @return 缓存是否设置成功
     */
    boolean set(String key, String value);

    /**
     * 获取缓存
     * @param key
     * @return
     *
     */
    String get(String key);

    /**
     * 设置过期时间
     *
     * @param key
     * @param seconds
     * @return
     */
    long expire(String key, long seconds);

    /**
     * 自增
     *
     * @param key
     */
    long incr(String key);
}
