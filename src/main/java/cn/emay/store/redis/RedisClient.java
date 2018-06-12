package cn.emay.store.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RedisClient {

	/* COMMON *************************************************/
	
	/**
	 * 是否存在
	 * 
	 * @param name
	 * @return
	 */
	public Boolean exists(String name);

	/**
	 * 删除
	 * 
	 * @param key
	 * @return
	 */
	public Boolean del(String... keys);

	/**
	 * 为key设置超时时间
	 * 
	 * @param key
	 * @param seconds
	 * @return
	 */
	public Boolean expire(String key, int seconds);

	/**
	 * 关闭
	 */
	public void close();

	/* COMMON *************************************************/

	/* HASH *************************************************/

	/**
	 * 删除hash中的多个字段
	 * 
	 * @param key
	 * @param fieldname
	 * @return
	 */
	public Boolean hdel(String key, String... fieldname);

	/**
	 * 往hash里面添加多个值
	 * 
	 * @param key
	 * @param value
	 * @param expireTime
	 * @return
	 */
	public Boolean hmset(String key, Map<String, Object> value, int expireTime);

	/**
	 * 往hash里面添加值
	 * 
	 * @param key
	 * @param fieldname
	 * @param value
	 * @param expireTime
	 * @return
	 */
	public Boolean hset(String key, String fieldname, Object value, int expireTime);

	/**
	 * 获取hash的总数
	 * 
	 * @param key
	 * @return
	 */
	public Long hlen(String key);

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

	/**
	 * hash中字段是否存在
	 * 
	 * @param key
	 * @param fieldname
	 * @return
	 */
	public Boolean hexists(String key, String fieldname);

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
	 * @param @param
	 *            key
	 * @param @param
	 *            fieldname
	 * @param @param
	 *            n
	 * @param @return
	 * @return Long
	 */
	public Long hIncrBy(String key, String fieldname, final long n);

	/* HASH *************************************************/

	/* KV *************************************************/

	/**
	 * 存储数据
	 * 
	 * @param key
	 * @param value
	 * @param expireTime
	 * @return
	 */
	public Boolean set(String key, Object value, int expireTime);

	/**
	 * 获取数据
	 * 
	 * @param key
	 * @return
	 */
	public String get(String key);

	/**
	 * 获取数据
	 * 
	 * @param key
	 * @return
	 */
	public Object getObject(String key);

	/**
	 * 获取数据,并将序列化的对象字符串转换为Object
	 * 
	 * @param key
	 * @param clazz
	 * @return
	 */
	public <T> T get(String key, Class<T> clazz);

	/**
	 * 存储数据【byte数组】
	 * 
	 * @param key
	 * @param value
	 * @param expireTime
	 * @return
	 */
	public Boolean setBytes(byte[] key, byte[] value, int expireTime);

	/**
	 * 获取数据[byte数组]
	 * 
	 * @param key
	 * @return
	 */
	public byte[] getBytes(byte[] key);

	/**
	 * 减一
	 * 
	 * @param key
	 * @return
	 */
	public Long decr(String key);

	/**
	 * 减n
	 * 
	 * @param key
	 * @param n
	 * @return
	 */
	public Long decrBy(String key, final long n);

	/**
	 * 加一
	 * 
	 * @param key
	 * @return
	 */
	public Long incr(String key);

	/**
	 * 加n
	 * 
	 * @param key
	 * @param n
	 * @return
	 */
	public Long incrBy(String key, final long n);

	/* KV *************************************************/

	/* LIST *************************************************/

	/**
	 * 往list左侧放数据
	 * 
	 * @param key
	 * @param object
	 * @param expireTime
	 * @return
	 */
	public Boolean lpush(String key, Object object, int expireTime);

	public Long lpush(String key, int expireTime, Object object);

	/**
	 * 往list右侧放数据
	 * 
	 * @param key
	 * @param object
	 * @param expireTime
	 * @return
	 */
	public Boolean rpush(String key, Object object, int expireTime);

	/**
	 * 往list左侧放数据
	 * 
	 * @param key
	 * @param objects
	 * @param expireTime
	 * @return
	 */
	public Boolean lpush(String key, int expireTime, Object... objects);

	/**
	 * 往list右侧放数据
	 * 
	 * @param key
	 * @param objects
	 * @param expireTime
	 * @return
	 */
	public Boolean rpush(String key, int expireTime, Object... objects);

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
	 * 从list左侧拿数据，并转换为特定class,阻塞
	 * 
	 * @param key
	 * @param blockingtimeout
	 * @param clazz
	 * @return
	 */
	public <T> T blpop(String key, int blockingtimeout, Class<T> clazz);

	/**
	 * 从list左侧拿数据,阻塞
	 * 
	 * @param key
	 * @param blockingtimeout
	 * @return
	 */
	public String blpop(String key, int blockingtimeout);

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
	 * 从list右侧拿数据，并转换为特定class,阻塞
	 * 
	 * @param key
	 * @param blockingtimeout
	 * @param clazz
	 * @return
	 */
	public <T> T brpop(String key, int blockingtimeout, Class<T> clazz);

	/**
	 * 从list右侧拿数据,阻塞
	 * 
	 * @param key
	 * @param blockingtimeout
	 * @return
	 */
	public String brpop(String key, int blockingtimeout);

	/**
	 * 获取队列所有数据 start,end指定取的范围 start=0,end=-1为取全部 start,end可为空，默认取全部
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @param clazz
	 * @return
	 */
	public <T> List<T> lrange(String key, long start, long end, Class<T> clazz);

	/**
	 * 获取队列所有数据 start,end指定取的范围 start=0,end=-1为取全部 start,end可为空，默认取全部
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public List<String> lrange(String key, Long start, Long end);

	/**
	 * list长度
	 * 
	 * @param key
	 * @return
	 */
	public Long llen(String key);

	/* LIST *************************************************/

	/* SET *************************************************/

	/**
	 * 往set中增加1个值
	 * 
	 * @param key
	 * @param value
	 * @param expireTime
	 * @return
	 */
	public Boolean sadd(String key, int expireTime, Object... values);

	/**
	 * 获取set的所有值 请自行强转
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
	 * 删除多个set的值
	 * 
	 * @param key
	 * @param members
	 * @return
	 */
	public Boolean srem(String key, String... members);

	/**
	 * 检测值是否在set中
	 * 
	 * @param key
	 * @param member
	 * @return
	 */
	public Boolean sismember(String key, String member);

	/**
	 * set 长度
	 * 
	 * @param key
	 * @return
	 */
	public Long scard(String key);

	/**
	 * 如果key存在就set值
	 * 
	 * @param key
	 * @param value
	 * @param expireTime
	 * @return
	 */
	public Boolean setnx(String key, Object value, int expireTime);

	/**
	 * @Title: srandmember
	 * @Description: 返回集合中一个随机元素
	 * @param @param
	 *            key
	 * @param @return
	 * @return String
	 */
	public String srandmember(String key);

	/**
	 * @Title: srandmember
	 * @Description: 返回集合中多个随机元素
	 * @param @param
	 *            key
	 * @param @param
	 *            count
	 * @param @return
	 * @return List<String>
	 */
	public List<String> srandmember(String key, int count);

	/* SET *************************************************/

	/* ZSET *************************************************/

	public long zadd(String key, Map<String, Double> scoreMembers);

	public long zadd(String key, double score, String member);

	public Set<String> zrangeByScore(String key, Double min, Double max);

	public Set<String> zrangeByScore(String key, double min, double max, int offset, int count);

	public long zrem(String key, String... members);

	public Long zremrangeByScore(String key, double start, double end);

	public Double zscore(String key, String member);

	public Long zrank(String key, String member);

	public Long zcard(String key);

	/* ZSET *************************************************/

}