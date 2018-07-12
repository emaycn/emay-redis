package cn.emay.store.redis.define;

public interface RedisCommon {
	
	/**
	 * 是否存在
	 * 
	 * @param name
	 * @return
	 */
	public boolean exists(String name);

	/**
	 * 删除
	 * 
	 * @param key
	 * @return
	 */
	public void del(String... keys);

	/**
	 * 为key设置超时时间
	 * 
	 * @param key
	 * @param seconds
	 * @return
	 */
	public void expire(String key, int seconds);

	/**
	 * 移除Key的超时时间
	 * 
	 * @param key
	 */
	public void persist(String key);

	/**
	 * 获取key的剩余超时时间[秒]<br/>
	 * 如果没有此key，或者key没有设置超时时间，则返回-1
	 * 
	 * @param key
	 * @return
	 */
	public long ttl(String key);

}
