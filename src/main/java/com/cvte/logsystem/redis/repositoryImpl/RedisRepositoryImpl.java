package com.cvte.logsystem.redis.repositoryImpl;

import com.cvte.logsystem.redis.repository.BasicRedisRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisRepositoryImpl implements Serializable, BasicRedisRepository {
    @Resource
    private RedisTemplate redisTemplate;

    // =================  Key-Value  ==================

    /**
     * 设置过期时间
     * @param key   键
     * @param time  过期时间
     * @return  是否设置成功
     */
    public boolean setKeyExpire(String key,long time){
        if(time > 0){
            return Boolean.TRUE.equals(redisTemplate.expire(key, time, TimeUnit.SECONDS));
        }
        return false;
    }

    /**
     * 获取过期时间
     * @param key   键
     * @return  过期时间
     */
    public long getKeyExpireTime(String key) throws NullPointerException{
        if(hasKey(key)){
            return redisTemplate.getExpire(key,TimeUnit.SECONDS);
        }
        return -1;
    }

    /**
     * 判断Key是否存在
     * @param key   键
     * @return  是否存在
     */
    public boolean hasKey(String key) throws NullPointerException{
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 删除key
     * @param keys  键
     */
    public void deleteKey(String... keys){
        if(keys != null && keys.length > 0){
            log.info(Arrays.toString(keys));
            if(keys.length == 1){
                redisTemplate.delete(keys[0]);
            }else{
                redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(keys));
            }
        }
    }

    /**
     * 获取缓存
     * @param key   键
     * @return  值
     */
    public Object getValue(String key){
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 添加缓存
     * @param key   键
     * @param value 值
     * @return  是否添加成功
     */
    public boolean setValue(String key,Object value){
        try{
            redisTemplate.opsForValue().set(key,value);
            return true;
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 添加缓存并设置过期时间
     * @param key   键
     * @param value 值
     * @param time  过期时间
     * @return  是否设置成功
     */
    public boolean setValue(String key,Object value,long time){
        try{
            redisTemplate.opsForValue().set(key,value);
            if (time > 0){
                setKeyExpire(key,time);
            }
            return true;
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

    // =================  Key-HashMap  ==================

    ///**
    // * 获取键值表中某个键对应的值
    // * @param key
    // * @param item
    // * @return
    // */
    //public Object getHashMapValue(String key,Object item){
    //    if(key == null || item == null || !hasKey(key)){
    //        return null;
    //    }
    //    return redisTemplate.opsForHash().get(key,item);
    //}
    //
    ///**
    // * 向哈希表中添加数据
    // * @param key
    // * @param item
    // * @param value
    // * @return
    // */
    //public boolean setHashMapValue(String key,String item,Object value){
    //    try{
    //        if (key == null || item == null){
    //            return false;
    //        }
    //        if (!hasKey(key)){
    //            setHashMap(key,new HashMap<>());
    //        }
    //        redisTemplate.opsForHash().put(key,item,value);
    //        return true;
    //    }catch (Exception e){
    //        log.error(e.getMessage());
    //        return false;
    //    }
    //}
    //
    ///**
    // * 获取某个键对应的哈希表
    // * @param key
    // * @return
    // */
    //public Map<String,Object> getHashMap(String key){
    //    return redisTemplate.opsForHash().entries(key);
    //}
    //
    ///**
    // * 设置 K-HashMap
    // * @param key
    // * @param map
    // * @return
    // */
    //public boolean setHashMap(String key,Map<String,Object> map){
    //    try{
    //        redisTemplate.opsForHash().putAll(key,map);
    //        return true;
    //    }catch (Exception e){
    //        log.error(e.getMessage());
    //        return false;
    //    }
    //}
    //
    ///**
    // * 设置 K - HashMap 并 设置过期时间
    // * @param key
    // * @param map
    // * @param time
    // * @return
    // */
    //public boolean setHashMap(String key,Map<String,Object> map,long time){
    //    try{
    //        redisTemplate.opsForHash().putAll(key,map);
    //        if(time > 0){
    //            setKeyExpire(key,time);
    //        }
    //        return true;
    //    }catch (Exception e){
    //        log.error(e.getMessage());
    //        return false;
    //    }
    //}
    //
    ///**
    // * 向哈希表中插入数据并设置过期时间
    // * @param key
    // * @param item
    // * @param value
    // * @param time
    // * @return
    // */
    //public boolean setHashMapValue(String key,Object item,Object value,long time){
    //    try{
    //        redisTemplate.opsForHash().put(key,item,value);
    //        if (time > 0){
    //            setKeyExpire(key,time);
    //        }
    //        return true;
    //    }catch (Exception e){
    //        log.error(e.getMessage());
    //        return false;
    //    }
    //}
    //
    ///**
    // * 删除哈希表中的值
    // * @param key
    // * @param items
    // */
    //public void deleteHashMapValue(String key,Object... items){
    //    if(key == null){
    //        throw new RuntimeException("删除的key值不能为空！");
    //    }
    //    redisTemplate.opsForHash().delete(key,items);
    //}
    //
    ///**
    // * 检查哈希表中是否存在item项
    // * @param key
    // * @param item
    // * @return
    // */
    //public boolean hashMapHasKey(String key,Object item){
    //    return redisTemplate.opsForHash().hasKey(key,item);
    //}

    // =================  Key-Set  ==================

    /**
     * 获取Set集合中的所有值
     * @param key   Set名称
     * @return  Set
     */
    public Set<Object> getSet(String key){
        try{
            return redisTemplate.opsForSet().members(key);
        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * 检查Set中是否存在该值
     * @param key   Set名称
     * @param value 数据
     * @return  是否存在
     */
    public boolean setHasKey(String key,Object value){
        try{
            return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, value));
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 将数据放入Set中
     * @param key   Set名称
     * @param values    数据
     * @return 成功个数
     */
    public long setSetValue(String key,Object... values){
        try{
            return redisTemplate.opsForSet().add(key,values);
        }catch (Exception e){
            log.error(e.getMessage());
            return 0;
        }
    }

    /**
     * 添加数据到Set中并设置过期时间
     * @param key   Set名称
     * @param time  过期时间
     * @param values    数据
     * @return  成功添加的数量
     */
    public long setSetValue(String key,long time,Object... values){
        try{
            Long cnt = redisTemplate.opsForSet().add(key,values);
            if(time > 0){
                setKeyExpire(key,time);
            }
            return cnt == null ? 0 : cnt;
        }catch (Exception e){
            log.error(e.getMessage());
            return 0;
        }
    }

    /**
     * 获取Set大小
     * @param key   Set名称
     * @return  Set大小
     */
    public long getSetSize(String key){
        try{
            return redisTemplate.opsForSet().size(key);
        }catch (Exception e){
            log.error(e.getMessage());
            return 0;
        }
    }

    /**
     * 从Set中移除values
     * @param key   Set名称
     * @param values    待删除数据
     * @return  成功移除数量
     */
    public long removeSetValue(String key,Object... values){
        try{
            return redisTemplate.opsForSet().remove(key,values);
        }catch (Exception e){
            log.error(e.getMessage());
            return 0;
        }
    }

    // =================  Key-List  ==================

    ///**
    // * 获取list中[start,end]的内容
    // * @param key
    // * @param start
    // * @param end
    // * @return
    // */
    //public List<Object> getList(String key,long start,long end){
    //    try{
    //        return redisTemplate.opsForList().range(key,start,end);
    //    }catch (Exception e){
    //        log.error(e.getMessage());
    //        return null;
    //    }
    //}
    //
    ///**
    // * 获取list的长度
    // * @param key
    // * @return
    // */
    //public long getListSize(String key){
    //    try{
    //        return redisTemplate.opsForList().size(key);
    //    }catch (Exception e){
    //        log.error(e.getMessage());
    //        return 0;
    //    }
    //}
    //
    ///**
    // * 获取list对应索引下的值
    // * @param key
    // * @param index
    // * @return
    // */
    //public Object getListValueByIndex(String key,long index){
    //    try{
    //        return redisTemplate.opsForList().index(key,index);
    //    }catch (Exception e){
    //        log.error(e.getMessage());
    //        return null;
    //    }
    //}
    //
    ///**
    // * 将值放入list
    // * @param key
    // * @param value
    // * @return
    // */
    //public boolean setListValue(String key,Object value){
    //    try{
    //        redisTemplate.opsForList().rightPush(key,value);
    //        return true;
    //    }catch (Exception e){
    //        log.error(e.getMessage());
    //        return false;
    //    }
    //}
    //
    ///**
    // * 将值放入list中并设置过期时间
    // * @param key
    // * @param value
    // * @param time
    // * @return
    // */
    //public boolean setListValue(String key,Object value,long time){
    //    try{
    //        redisTemplate.opsForList().rightPush(key,value);
    //        if (time > 0){
    //            setKeyExpire(key,time);
    //        }
    //        return true;
    //    }catch (Exception e){
    //        log.error(e.getMessage());
    //        return false;
    //    }
    //}
    //
    ///**
    // * 将集合list放入list中
    // * @param key
    // * @param list
    // * @return
    // */
    //public boolean setListValue(String key,Object... list){
    //    try{
    //        redisTemplate.opsForList().rightPushAll(key,list);
    //        return true;
    //    }catch (Exception e){
    //        log.error(e.getMessage());
    //        return false;
    //    }
    //}
    //
    ///**
    // * 将集合list放入list中,并设置过期时间
    // * @param key
    // * @param list
    // * @param time
    // * @return
    // */
    //public boolean setListValue(String key,long time,Object... list){
    //    try{
    //        redisTemplate.opsForList().rightPushAll(key,list);
    //        if (time > 0){
    //            setKeyExpire(key,time);
    //        }
    //        return true;
    //    }catch (Exception e){
    //        log.error(e.getMessage());
    //        return false;
    //    }
    //}
    //
    ///**
    // * 根据索引修改值
    // * @param key
    // * @param index
    // * @param value
    // * @return
    // */
    //public boolean updateListValueByIndex(String key,long index,Object value){
    //    try{
    //        redisTemplate.opsForList().set(key,index,value);
    //        return true;
    //    }catch (Exception e){
    //        log.error(e.getMessage());
    //        return false;
    //    }
    //}
    //
    ///**
    // * 删除N个value值
    // * @param key
    // * @param cnt
    // * @param value
    // * @return
    // */
    //public long removeListValue(String key,long cnt,Object value){
    //    try{
    //        return redisTemplate.opsForList().remove(key,cnt,value);
    //    }catch (Exception e){
    //        log.error(e.getMessage());
    //        return 0;
    //    }
    //}

    // =================  Key-ZSet  ==================

    /**
     * 获取ZSet集合中[start,end]值  [0,-1]表示全部区间
     * @param key   键
     * @param start 起始位置
     * @param end 结束位置
     * @return  [start,end]的set
     */
    public Set<Object> getZSet(String key,long start,long end){
        try{
            return redisTemplate.opsForZSet().range(key,start,end);
        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * 检查ZSet中是否存在该值
     * @param key   键
     * @param value 值
     * @return  是否存在该值
     */
    public boolean zsetHasKey(String key,Object value){
        try{
            return redisTemplate.opsForZSet().rank(key,value) != null;
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 将数据放入ZSet中
     * @param key   键
     * @param value 值
     * @return  是否添加成功
     */
    public boolean setZSetValue(String key,Object value,long score){
        try{
            return redisTemplate.opsForZSet().addIfAbsent(key,value,score);
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 一次性添加多条数据
     * @param key   键
     * @param values    值
     * @return  是否添加成功
     */
    public boolean setZSetValues(String key,Set values){
        try{
            Long cnt = redisTemplate.opsForZSet().add(key,values);
            return cnt != null && cnt == values.size();
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 添加数据到ZSet中并设置过期时间
     * @param key   键
     * @param time  过期时间
     * @param value 值
     * @return  是否添加成功
     */
    public boolean setZSetValue(String key,Object value,long time,long score){
        try{
            if (redisTemplate.opsForZSet().addIfAbsent(key,value,score)){
                if(time > 0){
                    setKeyExpire(key,time);
                }
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 获取ZSet大小
     * @param key   键
     * @return  set大小
     */
    public long getZSetSize(String key){
        try{
            return redisTemplate.opsForZSet().size(key);
        }catch (Exception e){
            log.error(e.getMessage());
            return 0;
        }
    }

    /**
     * 从ZSet中移除values
     * @param key   键
     * @param values    值
     * @return  成功添加的个数
     */
    public long removeZSetValue(String key,Object... values){
        try{
            return redisTemplate.opsForZSet().remove(key,values);
        }catch (Exception e){
            log.error(e.getMessage());
            return 0;
        }
    }

    /**
     * 根据score区间删除数据
     * @param key   键
     * @param minExpireTime 最小过期时间
     * @param maxExpireTime 最大过期时间
     * @return  成功删除的个数
     */
    public long removeZSetValueByScore(String key,long minExpireTime,long maxExpireTime){
        try{
            return redisTemplate.opsForZSet().removeRangeByScore(key,minExpireTime,maxExpireTime);
        }catch (Exception e){
            log.error(e.getMessage());
            return 0;
        }
    }

}
