package cn.emay.redis.define;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author Frank
 *
 */
public interface RedisSet {

	/**
	 * set 长度
	 * 
	 * @param key
	 * @return
	 */
	public long scard(String key);

	/**
	 * 往set中增加值
	 * 
	 * @param key
	 * @param value
	 * @param expireTime
	 * @return
	 */
	public long sadd(String key, int expireTime, Object value);
	
	/**
	 * 往set中增加值
	 * 
	 * @param key
	 * @param values
	 * @param expireTime
	 * @return
	 */
	public long sadd(String key, int expireTime, Object[] values);
	
	/**
	 * 往set中增加值
	 * 
	 * @param key
	 * @param values
	 * @param expireTime
	 * @return
	 */
	public long sadd(String key, int expireTime, Collection<?> values);

	/**
	 * 随机获取并删除一个set的值
	 * 
	 * @param key
	 * @param clazz
	 * @return
	 */
	public <T> T spop(String key, Class<T> clazz);

	/**
	 * 随机获取并删除一个set的值
	 * 
	 * @param key
	 * @return
	 */
	public String spop(String key);

	/**
	 * 返回集合中一个随机元素，不删除
	 * 
	 * @param key
	 * @param clazz
	 * @return
	 */
	public <T> T srandmember(String key, Class<T> clazz);

	/**
	 * 返回集合中一个随机元素，不删除
	 * 
	 * @param key
	 * @return
	 */
	public String srandmember(String key);

	/**
	 * 返回集合中多个随机元素，不删除
	 * 
	 * @param key
	 * @param count
	 * @return
	 */
	public List<String> srandmember(String key, int count);

	/**
	 * 返回集合中多个随机元素，不删除
	 * 
	 * @param key
	 * @param count
	 * @param clazz
	 * @return
	 */
	public <T> List<T> srandmember(String key, int count, Class<T> clazz);

	/**
	 * 获取set的所有值
	 * 
	 * @param key
	 * @param clazz
	 * @return
	 */
	public <T> Set<T> smembers(String key, Class<T> clazz);

	/**
	 * 获取set的所有值
	 * 
	 * @param key
	 * @return
	 */
	public Set<String> smembers(String key);

	/**
	 * 删除set的值
	 * 
	 * @param key
	 * @param member
	 * @return
	 */
	public long srem(String key, Object member);
	
	/**
	 * 删除多个set的值
	 * 
	 * @param key
	 * @param members
	 * @return
	 */
	public long srem(String key, Object[] members);
	
	/**
	 * 删除多个set的值
	 * 
	 * @param key
	 * @param members
	 * @return
	 */
	public long srem(String key, Collection<?> members);

	/**
	 * 检测值是否在set中
	 * 
	 * @param key
	 * @param member
	 * @return
	 */
	public boolean sismember(String key, Object member);

}