package cn.emay.redis.define;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Frank
 */
public interface RedisSet {

    /**
     * set 长度
     *
     * @param key 键
     * @return 长度
     */
    long scard(String key);

    /**
     * 往set中增加值
     *
     * @param key        键
     * @param value      值
     * @param expireTime 超时时间
     * @return 长度
     */
    long sadd(String key, int expireTime, Object value);

    /**
     * 往set中增加值
     *
     * @param key        键
     * @param values     值
     * @param expireTime 超时时间
     * @return 长度
     */
    long sadd(String key, int expireTime, Object[] values);

    /**
     * 往set中增加值
     *
     * @param key        键
     * @param values     值
     * @param expireTime 超时时间
     * @return 长度
     */
    long sadd(String key, int expireTime, Collection<?> values);

    /**
     * 随机获取并删除一个set的值
     *
     * @param key   键
     * @param clazz 类型
     * @return 元素
     */
    <T> T spop(String key, Class<T> clazz);

    /**
     * 随机获取并删除一个set的值
     *
     * @param key 键
     * @return 元素
     */
    String spop(String key);

    /**
     * 返回集合中一个随机元素，不删除
     *
     * @param key   键
     * @param clazz 类型
     * @return 元素
     */
    <T> T srandmember(String key, Class<T> clazz);

    /**
     * 返回集合中一个随机元素，不删除
     *
     * @param key 键
     * @return 元素
     */
    String srandmember(String key);

    /**
     * 返回集合中多个随机元素，不删除
     *
     * @param key   键
     * @param count 个数
     * @return 元素
     */
    List<String> srandmember(String key, int count);

    /**
     * 返回集合中多个随机元素，不删除
     *
     * @param key   键
     * @param count 个数
     * @param clazz 类型
     * @return 元素
     */
    <T> List<T> srandmember(String key, int count, Class<T> clazz);

    /**
     * 获取set的所有值
     *
     * @param key   键
     * @param clazz 类型
     * @return 元素
     */
    <T> Set<T> smembers(String key, Class<T> clazz);

    /**
     * 获取set的所有值
     *
     * @param key 键
     * @return 元素
     */
    Set<String> smembers(String key);

    /**
     * 删除set的值
     *
     * @param key    键
     * @param member 成员
     * @return 删除的数量
     */
    long srem(String key, Object member);

    /**
     * 删除多个set的值
     *
     * @param key     键
     * @param members 成员
     * @return 删除的数量
     */
    long srem(String key, Object[] members);

    /**
     * 删除多个set的值
     *
     * @param key     键
     * @param members 成员
     * @return 删除的数量
     */
    long srem(String key, Collection<?> members);

    /**
     * 检测值是否在set中
     *
     * @param key    键
     * @param member 成员
     * @return 值是否在set中
     */
    boolean sismember(String key, Object member);

}
