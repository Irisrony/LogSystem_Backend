package com.cvte.logsystem.db_mongo.repositoryImpl;

import com.cvte.logsystem.db_mongo.repository.BasicMongoRepository;


import com.cvte.logsystem.domain.LogInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Description TODO
 * @Classname MongoRepositoryImpl
 * @Date 2023/8/2 5:34 PM
 * @Created by liushenghao
 */
@Repository
@Slf4j
public class MongoRepositoryImpl implements BasicMongoRepository<LogInfo> {
    @Autowired
    private MongoTemplate mongoTemplate;

    // =========== 获取全部集合名称 =============

    @Override
    public Set<String> getCollectionNames(){
        return mongoTemplate.getCollectionNames();
    }

    @Override
    public Boolean hasCollectionName(String appid){
        return mongoTemplate.collectionExists(appid);
    }

    // =========== 添加/更新数据 =============

    /**
     * 单个数据新增(有则更新，无则新增)
     * @param key   键
     * @param value 值
     * @param updateKey 更新字段
     * @param updateValue   更新字段值
     * @param className Entity.class
     * @param collectionName    集合名
     */
    @Override
    public void singleUpsert(String key,String value,String updateKey,Object updateValue,Class className,String collectionName){
        Query query = Query.query(Criteria.where(key).is(value));
        findOneByQuery(query,className,collectionName);
        Update update = Update.update(updateKey,updateValue);
        mongoTemplate.upsert(query,update,className,collectionName);
    }

    /**
     * 保存单个数据
     * @param obj
     * @param collectionName
     */
    @Override
    public void save(LogInfo obj,String collectionName){
        mongoTemplate.save(obj,collectionName);
    }

    @Override
    public void insert(List<LogInfo> list,String collectionName){
        mongoTemplate.insert(list,collectionName);
    }

    // =========== 条件查询 =============

    // 1. 字段查询

    /**
     * 单个集合查询同一字段
     * @param key   键
     * @param value 值
     * @param className Entity.class
     * @param collectionName    集合名称(${appid})
     * @return  Entity
     */
    @Override
    public LogInfo findOneByField(String key,String value,Class className,String collectionName){
        Query query = Query.query(Criteria.where(key).is(value));
        return (LogInfo) mongoTemplate.findOne(query,className,collectionName);
    }

    /**
     * 在所有集合中查询同一字段
     * @param key   键
     * @param value 值
     * @param className Entity.class
     * @return  该id在所有集合中的数据集合
     */
    @Override
    public List<LogInfo> findAllByField(String key, String value, Class className){
        Query query = Query.query(Criteria.where(key).is(value));
        List<Object> res = new ArrayList<>();
        Set<String> set = getCollectionNames();
        if(set != null){
            set.forEach(s -> {
                res.addAll(mongoTemplate.find(query,className,s));
            });
        }
        return res.stream().filter(Objects::nonNull).map(s -> (LogInfo) s).toList();
    }

    @Override
    public LogInfo findById(Object id,Class className,String collectionName){
        return (LogInfo) mongoTemplate.findById(id,className,collectionName);
    }

    // 2. Sql查询

    /**
     * 单个集合Sql查询单个对象
     * @param query Sql
     * @param className Entity.class
     * @param collectionName    集合名称
     * @return  单个集合中符合条件的单个对象
     */
    @Override
    public LogInfo findOneByQuery(Query query,Class className,String collectionName){
        return (LogInfo) mongoTemplate.findOne(query,className,collectionName);
    }

    /**
     * 单个集合中sql查询全部
     * @param query sql
     * @param className Entity.class
     * @param collectionName    集合名称
     * @return  单个集合中所有满足条件的对象的集合
     */
    @Override
    public List<LogInfo> findAllByQuery(Query query,Class className,String collectionName){
        return mongoTemplate.find(query,className,collectionName);
    }

    /**
     * 全部集合中sql查询全部
     * @param query Sql
     * @param className Entity.class
     * @return  所有集合中满足条件的所有对象的集合
     */
    @Override
    public List<LogInfo> findAllByQuery(Query query,Class className){
        Set<String> set = getCollectionNames();
        List<Object> res = new ArrayList<>();
        if(set != null){
            set.forEach(s -> {
                res.addAll(mongoTemplate.find(query,className,s));
            });
        }
        return res.stream().filter(Objects::nonNull).map(s -> (LogInfo)s).toList();
    }

    // 3. 查询全部数据

    /**
     * 单个集合查询全部数据
     * @param className     Entity.class
     * @param collectionName    集合名称
     * @return  该集合的所有数据的集合
     */
    @Override
    public List<LogInfo> findAll(Class className,String collectionName){
        return mongoTemplate.findAll(className,collectionName);
    }

    /**
     * 全部集合查询全部数据
     * @param   className   Entity.class
     * @return  所有数据的集合
     */
    @Override
    public List<LogInfo> findAll(Class className){
        Set<String> set = getCollectionNames();
        List<Object> res = new ArrayList<>();
        if(set != null){
            set.forEach(s -> {
                res.addAll(mongoTemplate.findAll(className,s));
            });
        }
        return res.stream().filter(Objects::nonNull).map(s -> (LogInfo)s).collect(Collectors.toList());
    }

    // 4. 单字段查询

    public List<LogInfo> findOneField(String field,Class className,String collectionName){
        Query query = new Query();
        query.fields().include(field);
        return mongoTemplate.find(query,className,collectionName);
    }

    // 5. 分页查询

    /**
     * 单集合分页查询
     * @param pageNum   页码
     * @param pageSize  页面大小
     * @param className Entity.class
     * @param collectionName    集合名称
     * @return  分页查询出的对象的集合
     */
    @Override
    public List<LogInfo> findByPage(int pageNum,int pageSize,Class className,String collectionName){
        Pageable pageable = PageRequest.of(pageNum-1,pageSize);
        Query query = new Query().with(pageable);
        return mongoTemplate.find(query,className,collectionName);
    }

    // 6. 综合查询

    /**
     * 综合查询
     * @param pageNum   页码
     * @param pageSize  每页数量
     * @param key   键
     * @param value 值
     * @param regexTag  模糊查询字段
     * @param content   模糊查询
     * @param className Entity.class
     * @param collectionName    集合名
     * @return  所有满足查询条件的对象的集合
     */
    @Override
    public List<LogInfo> findAllByPage(int pageNum,int pageSize,String key,String value,String regexTag,String content,Class className,String collectionName){
        Query query = new Query();
        query.with(Sort.by(Sort.Order.desc("_id")));
        // userid
        if(Strings.isNotBlank(key) && Strings.isNotBlank(value)){
            query.addCriteria(Criteria.where(key).is(value));
        }
        // 模糊查询
        if (Strings.isNotBlank(regexTag) && Strings.isNotBlank(content)){
            Pattern pattern = Pattern.compile("^.*" + content + ".*$",Pattern.CASE_INSENSITIVE);
            query.addCriteria(Criteria.where(regexTag).regex(pattern));
        }
        // 集合名(appid)
        if (Strings.isNotBlank(collectionName)){
            query.with(PageRequest.of(pageNum-1,pageSize));
            return findAllByQuery(query,className,collectionName);
        }
        return findAllByQuery(query,className).stream().skip((long) (pageNum - 1) * pageSize).limit(pageSize).toList();
    }

    // =========== 数据删除 ==============

    /**
     * 删除对象
     * @param key   键
     * @param value 值
     * @param className Entity.class
     * @param collectionName    集合名称
     */
    @Override
    public void singleRemove(String key,String value,Class className,String collectionName){
        Query query = Query.query(Criteria.where(key).is(key));
        mongoTemplate.remove(query,className,collectionName);
    }
}