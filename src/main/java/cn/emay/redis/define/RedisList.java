package cn.emay.redis.define;

import java.util.List;

/**
 * 
 * @author Frank
 *
 */
public interface RedisList {

	/**
	 * list长度
	 * 
	 * @param key
	 * @return
	 */
	public long llen(String key);

	/**
	 * 往list左侧放数据
	 * 
	 * @param key
	 * @param objects
	 * @param expireTime
	 * @return 队列长度
	 */
	public long lpush(String key, int expireTime, Object... objects);

	/**
	 * 往list右侧放数据
	 * 
	 * @param key
	 * @param objects
	 * @param expireTime
	 * @return 队列长度
	 */
	public long rpush(String key, int expireTime, Object... objects);

	/**
	 * 从list左侧拿数据，并转换为特定class,无阻塞
	 * 
	 * @param key
	 * @param clazz
	 * @return
	 */
	public <T> T lpop(String key, Class<T> clazz);

	/**
	 * 从list左侧拿数据,无阻塞
	 * 
	 * @param key
	 * @return
	 */
	public String lpop(String key);

	/**
	 * 从list右侧拿数据，并转换为特定class,无阻塞
	 * 
	 * @param key
	 * @param clazz
	 * @return
	 */
	public <T> T rpop(String key, Class<T> clazz);

	/**
	 * 从list右侧拿数据,无阻塞
	 * 
	 * @param key
	 * @return
	 */
	public String rpop(String key);

	/**
	 * 获取队列所有数据 <br/>
	 * start,end指定取的范围 start=0,end=-1为取全部 <br/>
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @param clazz
	 * @return
	 */
	public <T> List<T> lrange(String key, long start, long end, Class<T> clazz);

	/**
	 * 获取队列所有数据 <br/>
	 * start,end指定取的范围 start=0,end=-1为取全部 <br/>
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public List<String> lrange(String key, long start, long end);

}
