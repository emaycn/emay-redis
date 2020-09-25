package cn.emay.redis.define;

/**
 * @author Frank
 */
public interface RedisString {

    /**
     * 存储数据<br/>
     * 非String、byte[]的类型，将会被JsonHelper序列化<br/>
     *
     * @param key        键
     * @param value      值
     * @param expireTime 超时时间
     * @return 是否成功
     */
    boolean set(String key, Object value, int expireTime);

    /**
     * 存储数据[如果不存在则set]<br/>
     * 非String、byte[]的类型，将会被JsonHelper序列化<br/>
     *
     * @param key        键
     * @param value      值
     * @param expireTime 超时时间
     * @return 是否成功
     */
    boolean setnx(String key, Object value, int expireTime);

    /**
     * 获取数据
     *
     * @param key 键
     * @return 数据
     */
    String get(String key);

    /**
     * 获取数据[指定类型]<br/>
     *
     * @param key   键
     * @param clazz 类型
     * @return 数据
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * 减一
     *
     * @param key 键
     * @return 值
     */
    long decr(String key);

    /**
     * 减n
     *
     * @param key    键
     * @param number 减去的值
     * @return 值
     */
    long decrBy(String key, long number);

    /**
     * 加一
     *
     * @param key 键
     * @return 值
     */
    long incr(String key);

    /**
     * 加n
     *
     * @param key    键
     * @param number 加上的值
     * @return 值
     */
    long incrBy(String key, long number);

}
