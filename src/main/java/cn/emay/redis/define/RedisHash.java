package cn.emay.redis.define;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Frank
 */
public interface RedisHash {

    /**
     * 删除hash中的多个字段
     *
     * @param key       键
     * @param fieldname 字段
     * @return 删除的数量
     */
    long hdel(String key, String... fieldname);

    /**
     * 获取hash的总数
     *
     * @param key 键
     * @return hash字段总数
     */
    long hlen(String key);

    /**
     * hash中字段是否存在
     *
     * @param key       键
     * @param fieldname 字段
     * @return 是否存在
     */
    boolean hexists(String key, String fieldname);

    /**
     * hash中所有的field name
     *
     * @param key 键
     * @return 所有field
     */
    Set<String> hkeys(String key);

    /**
     * hash计数器加n
     *
     * @param key       键
     * @param fieldname 字段
     * @param number    数
     * @return 值
     */
    long hIncrBy(String key, String fieldname, long number);

    /**
     * 往hash里面添加值<br/>
     * 非String、byte[]的类型，将会被JsonHelper序列化<br/>
     *
     * @param key        键
     * @param fieldname  字段
     * @param value      值
     * @param expireTime 超时时间
     * @return 是否成功
     */
    boolean hset(String key, String fieldname, Object value, int expireTime);

    /**
     * 往hash里面添加值[如果不存在则set]<br/>
     * 非String、byte[]的类型，将会被JsonHelper序列化<br/>
     *
     * @param key        键
     * @param fieldname  字段
     * @param value      值
     * @param expireTime 超时时间
     * @return 是否成功
     */
    boolean hsetnx(String key, String fieldname, Object value, int expireTime);

    /**
     * 往hash里面添加多个值<br/>
     * 非String、byte[]的类型，将会被JsonHelper序列化<br/>
     *
     * @param key        键
     * @param value      值
     * @param expireTime 超时时间
     * @return 是否成功
     */
    boolean hmset(String key, Map<String, ?> value, int expireTime);

    /**
     * 获取hash field的值
     *
     * @param key       键
     * @param fieldname 字段
     * @param clazz     类型
     * @return 值
     */
    <T> T hget(String key, String fieldname, Class<T> clazz);

    /**
     * 获取hash field的值
     *
     * @param key       键
     * @param fieldname 字段
     * @return 值
     */
    String hget(String key, String fieldname);

    /**
     * 获取多个hash field的值
     *
     * @param key        键
     * @param clazz      类型
     * @param fieldnames 字段
     * @return 值
     */
    <T> List<T> hmget(String key, Class<T> clazz, String... fieldnames);

    /**
     * 获取多个hash field的值
     *
     * @param key        键
     * @param fieldnames 字段
     * @return 值
     */
    List<String> hmget(String key, String... fieldnames);

    /**
     * 获取hash 所有field的值 已经序列化为对象
     *
     * @param key   键
     * @param clazz 类型
     * @return 键值对
     */
    <T> Map<String, T> hgetAll(String key, Class<T> clazz);

    /**
     * 获取hash 所有field的值
     *
     * @param key 键
     * @return 键值对
     */
    Map<String, String> hgetAll(String key);

}
