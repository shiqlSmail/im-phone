package com.im.phone.server.cache;

import java.util.Map;
import java.util.Set;

public interface  ICacheManager {
    /**
     * 存入缓存
     * @param key
     * @param cache
     */
    void putCache(String key, CacheManagerEntity cache);

    /**
     * 存入缓存
     * @param key
     * @param datas
     * @param timeOut
     */
    void putCache(String key, Object datas, long timeOut);

    /**
     * 获取对应缓存
     * @param key
     * @return
     */
    CacheManagerEntity getCacheByKey(String key);

    /**
     * 获取对应缓存
     * @param key
     * @return
     */
    Object getCacheDataByKey(String key);

    /**
     * 获取所有缓存
     * @return
     */
    Map<String, CacheManagerEntity> getCacheAll();

    /**
     * 判断是否在缓存中
     * @param key
     * @return
     */
    boolean isContains(String key);

    /**
     * 清除所有缓存
     */
    void clearAll();

    /**
     * 清除对应缓存
     * @param key
     */
    void clearByKey(String key);

    /**
     * 缓存是否为空
     */
    boolean isEmpty();

    /**
     * 缓存是否超时失效
     * @param key
     * @return
     */
    boolean isTimeOut(String key);

    /**
     * 获取所有key
     * @return
     */
    Set<String> getAllKeys();
}
