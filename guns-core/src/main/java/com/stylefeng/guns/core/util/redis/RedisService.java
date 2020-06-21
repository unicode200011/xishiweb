package com.stylefeng.guns.core.util.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 入缓存
     *
     * @param key
     * @param o
     * @return
     */
    public boolean toCache(String key, Object o) {
        return set(key, o);
    }

    /**
     * 入缓存 失效时间 单位秒
     *
     * @param key
     * @param o
     * @return
     */
    public boolean toCache(String key, Object o, long time) {
        return set(key, o, time);
    }

    /**
     * 取
     *
     * @param key
     * @return
     */

    public Object getFromCache(String key) {
        return get(key);
    }


    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入缓存设置时效时间
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 加减
     *
     * @return
     */
    public Long incrAndDecr(final String key, Long incrOrDecrNum) {
        ValueOperations operations = redisTemplate.opsForValue();
        Long count = operations.increment(key, incrOrDecrNum);
        return count;
    }

    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除key
     *
     * @param pattern
     */
    public void removePattern(final String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0)
            redisTemplate.delete(keys);
    }

    /**
     * 删除对应的value
     *
     * @param key
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 删除对应的value，并返回是否删除成功
     *
     * @param key
     */
    public boolean removeAndReturn(final String key) {
        if (exists(key)) {
            return redisTemplate.getConnectionFactory().getConnection().del(key.getBytes()) == 1;
        }
        return false;
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public Object get(final String key) {
        Object result = null;
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    /**
     * 哈希 添加
     *
     * @param key
     * @param hashKey
     * @param value
     */
    public void hmSet(String key, Object hashKey, Object value) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.put(key, hashKey, value);
    }

    /**
     * 哈希获取数据
     *
     * @param key
     * @param hashKey
     * @return
     */
    public Object hmGet(String key, Object hashKey) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.get(key, hashKey);
    }

    /**
     * 列表添加
     *
     * @param k
     * @param v
     */
    public void lPush(String k, Object v) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        list.rightPush(k, v);
    }

    /**
     * 列表获取
     *
     * @param k
     * @param l
     * @param l1
     * @return
     */
    public List<Object> lRange(String k, long l, long l1) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.range(k, l, l1);
    }

    /**
     * 集合添加
     *
     * @param key
     * @param value
     */
    public Long add(String key, Object value) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        Long result = set.add(key, value);
        return result;
    }

    /**
     * 集合获取
     *
     * @param key
     * @return
     */
    public Set<Object> setMembers(String key) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.members(key);
    }

    /**
     * 有序集合添加
     *
     * @param key
     * @param value
     * @param scoure
     */
    public void zAdd(String key, Object value, double scoure) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.add(key, value, scoure);
    }

    /**
     * 有序集合获取
     *
     * @param key
     * @param scoure
     * @param scoure1
     * @return
     */
    public Set<Object> rangeByScore(String key, double scoure, double scoure1) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rangeByScore(key, scoure, scoure1);
    }

    /**
     * 设置KEY的生命周期。如果该KEY被修改过了，这个KEY的生命周期限制就不存在了。
     *
     * @param key
     * @param seconds
     * @param timeUnit
     * @author kimi
     * @dateTime 2013-1-30 下午5:47:06
     */
    public boolean setExpireKey(String key, Long seconds, TimeUnit timeUnit) {
        return redisTemplate.expire(key, seconds, timeUnit);
    }

    /**
     * @description: 获取key剩余过期时间;
     * @author: lx
     */
    public Long getExpireByKey(String key) {
        return redisTemplate.getExpire(key);
    }


    /**
     * @param key
     * @param values
     * @description: lpushAll;
     */
    public Long leftPushAll(Object key, Collection values) {
        ListOperations list = redisTemplate.opsForList();
        Long result = list.leftPushAll(key, values);
        return result;
    }

    /**
     * list 移除,参考lrem函数
     * count = 0 : 全部value删除
     * 否则删除count个value
     *
     * @param key
     * @param count
     * @param value
     * @return
     */
    public Long lrem(String key, long count, Object value) {
        ListOperations list = redisTemplate.opsForList();
        Long result = list.remove(key, count, value);
        return result;
    }

    /**
     * @param key
     * @param values
     * @description: rightPushAll;
     */
    public Long rightPushAll(String key, Collection values) {
        ListOperations list = redisTemplate.opsForList();
        Long result = list.rightPushAll(key, values);
        return result;
    }

    /**
     * srem 删除set中的一个元素
     *
     * @param key
     * @param values
     * @return 删除的个数
     */
    public Long srem(String key, Object... values) {
        SetOperations setOperations = redisTemplate.opsForSet();
        Long result = setOperations.remove(key, values);
        return result;
    }

    /**
     * 列表添加 插入到列表头部
     *
     * @param k
     * @param v
     */
    public void leftPush(String k, Object v) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        list.leftPush(k, v);
    }

    /**
     * 列表添加 插入到列表头部 所有
     *
     * @param k
     * @param v
     */
    public void leftPushAll(String k, Collection v) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        list.leftPushAll(k, v);
    }
}
