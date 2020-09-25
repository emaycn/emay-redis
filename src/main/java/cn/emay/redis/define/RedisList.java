package cn.emay.redis.define;

import java.util.Collection;
import java.util.List;

/**
 * @author Frank
 */
public interface RedisList {

    /**
     * list长度
     *
     * @param key 键
     * @return 长度
     */
    long llen(String key);

    /**
     * 往list左侧批量放数据
     *
     * @param key        键
     * @param objects    数据
     * @param expireTime 超时时间
     * @return 队列长度
     */
    long lpush(String key, int expireTime, Collection<?> objects);

    /**
     * 往list左侧批量放数据
     *
     * @param key        键
     * @param objects    数据
     * @param expireTime 超时时间
     * @return 队列长度
     */
    long lpush(String key, int expireTime, Object[] objects);

    /**
     * 往list左侧放数据
     *
     * @param key        键
     * @param object     数据
     * @param expireTime 超时时间
     * @return 队列长度
     */
    long lpush(String key, int expireTime, Object object);

    /**
     * 往list右侧批量放数据
     *
     * @param key        键
     * @param objects    数据
     * @param expireTime 超时时间
     * @return 队列长度
     */
    long rpush(String key, int expireTime, Collection<?> objects);

    /**
     * 往list右侧批量放数据
     *
     * @param key        键
     * @param objects    数据
     * @param expireTime 超时时间
     * @return 队列长度
     */
    long rpush(String key, int expireTime, Object[] objects);

    /**
     * 往list右侧放数据
     *
     * @param key        键
     * @param object     数据
     * @param expireTime 超时时间
     * @return 队列长度
     */
    long rpush(String key, int expireTime, Object object);

    /**
     * 从list左侧拿数据，并转换为特定class,无阻塞
     *
     * @param key   键
     * @param clazz 类型
     * @return 值
     */
    <T> T lpop(String key, Class<T> clazz);

    /**
     * 从list左侧拿数据,无阻塞
     *
     * @param key 键
     * @return 值
     */
    String lpop(String key);

    /**
     * 从list右侧拿数据，并转换为特定class,无阻塞
     *
     * @param key   键
     * @param clazz 类型
     * @return 值
     */
    <T> T rpop(String key, Class<T> clazz);

    /**
     * 从list右侧拿数据,无阻塞
     *
     * @param key 键
     * @return 值
     */
    String rpop(String key);

    /**
     * 获取队列所有数据 <br/>
     * start,end指定取的范围 start=0,end=-1为取全部 <br/>
     *
     * @param key   键
     * @param start 起始位置
     * @param end   结束位置
     * @param clazz 类型
     * @return 数据
     */
    <T> List<T> lrange(String key, long start, long end, Class<T> clazz);

    /**
     * 获取队列所有数据 <br/>
     * start,end指定取的范围 start=0,end=-1为取全部 <br/>
     *
     * @param key   键
     * @param start 起始位置
     * @param end   结束位置
     * @return 数据
     */
    List<String> lrange(String key, long start, long end);

}
