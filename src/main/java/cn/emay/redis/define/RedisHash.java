package cn.emay.redis.define;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Frank
 *
 */
public interface RedisHash {

	/**
	 * 删除hash中的多个字段
	 * 
	 * @param key
	 * @param fieldname
	 * @return
	 */
	public long hdel(String key, String... fieldname);

	/**
	 * 获取hash的总数
	 * 
	 * @param key
	 * @return
	 */
	public long hlen(String key);

	/**
	 * hash中字段是否存在
	 * 
	 * @param key
	 * @param fieldname
	 * @return
	 */
	public boolean hexists(String key, String fieldname);

	/**
	 * hash中所有的field name
	 * 
	 * @param key
	 * @return
	 */
	public Set<String> hkeys(String key);

	/**
	 * hash计数器加n
	 * 
	 * @param key
	 * @param fieldname
	 * @param number
	 * @return long
	 */
	public long hIncrBy(String key, String fieldname, long number);

	/**
	 * 往hash里面添加值<br/>
	 * 非String、byte[]的类型，将会被JsonHelper序列化<br/>
	 * 
	 * @param key
	 * @param fieldname
	 * @param value
	 * @param expireTime
	 * @return
	 */
	public boolean hset(String key, String fieldname, Object value, int expireTime);

	/**
	 * 往hash里面添加值[如果不存在则set]<br/>
	 * 非String、byte[]的类型，将会被JsonHelper序列化<br/>
	 * 
	 * @param key
	 * @param fieldname
	 * @param value
	 * @param expireTime
	 * @return
	 */
	public boolean hsetnx(String key, String fieldname, Object value, int expireTime);

	/**
	 * 往hash里面添加多个值<br/>
	 * 非String、byte[]的类型，将会被JsonHelper序列化<br/>
	 * 
	 * @param key
	 * @param value
	 * @param expireTime
	 * @return
	 */
	public boolean hmset(String key, Map<String, ?> value, int expireTime);

	/**
	 * 获取hash field的值
	 * 
	 * @param key
	 * @param fieldname
	 * @param clazz
	 * @return
	 */
	public <T> T hget(String key, String fieldname, Class<T> clazz);

	/**
	 * 获取hash field的值
	 * 
	 * @param key
	 * @param fieldname
	 * @return
	 */
	public String hget(String key, String fieldname);

	/**
	 * 获取多个hash field的值
	 * 
	 * @param key
	 * @param clazz
	 * @param fieldnames
	 * @return
	 */
	public <T> List<T> hmget(String key, Class<T> clazz, String... fieldnames);

	/**
	 * 获取多个hash field的值
	 * 
	 * @param key
	 * @param fieldnames
	 * @return
	 */
	public List<String> hmget(String key, String... fieldnames);

	/**
	 * 获取hash 所有field的值 已经序列化为对象
	 * 
	 * @param key
	 * @param clazz
	 * @return
	 */
	public <T> Map<String, T> hgetAll(String key, Class<T> clazz);

	/**
	 * 获取hash 所有field的值
	 * 
	 * @param key
	 * @return
	 */
	public Map<String, String> hgetAll(String key);

}
