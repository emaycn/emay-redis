package cn.emay.store.redis.define;

public interface RedisString {
	
	/**
	 * 存储数据<br/>
	 * 非String、byte[]的类型，将会被JsonHelper序列化<br/>
	 * 
	 * @param key
	 * @param value
	 * @param expireTime
	 * @return
	 */
	public void set(String key, Object value, int expireTime);

	/**
	 * 存储数据[如果不存在则set]<br/>
	 * 非String、byte[]的类型，将会被JsonHelper序列化<br/>
	 * 
	 * @param key
	 * @param value
	 * @param expireTime
	 * @return
	 */
	public boolean setnx(String key, Object value, int expireTime);

	/**
	 * 获取数据
	 * 
	 * @param key
	 * @return
	 */
	public String get(String key);

	/**
	 * 获取数据[指定类型]<br/>
	 * 
	 * @param key
	 * @param clazz
	 * @return
	 */
	public <T> T get(String key, Class<T> clazz);

	/**
	 * 减一
	 * 
	 * @param key
	 * @return
	 */
	public long decr(String key);

	/**
	 * 减n
	 * 
	 * @param key
	 * @param number
	 * @return
	 */
	public long decrBy(String key, long number);

	/**
	 * 加一
	 * 
	 * @param key
	 * @return
	 */
	public long incr(String key);

	/**
	 * 加n
	 * 
	 * @param key
	 * @param number
	 * @return
	 */
	public long incrBy(String key, long number);

}
