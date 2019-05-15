package cn.emay.redis.define;

/**
 * Redis基础操作
 * 
 * @author Frank
 *
 */
public interface RedisCommon {

	/**
	 * 是否存在
	 * 
	 * @param key
	 * @return 是否存在
	 */
	public boolean exists(String key);

	/**
	 * 删除
	 * 
	 * @param keys
	 * @return 删除成功的个数
	 */
	public long del(String... keys);

	/**
	 * 为key设置超时时间
	 * 
	 * @param key
	 * @param seconds
	 *            超时时间(秒)
	 * @return 设置成功的个数
	 */
	public boolean expire(String key, int seconds);

	/**
	 * 移除Key的超时时间
	 * 
	 * @param key
	 * @return 移除成功的个数
	 */
	public boolean persist(String key);

	/**
	 * 获取key的剩余超时时间[秒]<br/>
	 * 
	 * @param key
	 * @return 如果没有此key，或者key没有设置超时时间，则返回-1
	 */
	public long ttl(String key);

}
