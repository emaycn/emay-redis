package cn.emay.store.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.emay.store.redis.command.RedisCommand;

/**
 * Redis 客户端
 * 
 * @author Frank
 *
 */
public interface RedisClient {

	/* BASE */

	/**
	 * 执行
	 * 
	 * @param command
	 * @return
	 */
	public <T> T exec(RedisCommand<T> command);

	/**
	 * 获取日期处理格式
	 * 
	 * @return
	 */
	public String getDatePattern();

	/* COMMON */

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

	/* String */

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

	/* HASH */

	/**
	 * 删除hash中的多个字段
	 * 
	 * @param key
	 * @param fieldname
	 * @return
	 */
	public void hdel(String key, String... fieldname);

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
	 * @param n
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
	public void hset(String key, String fieldname, Object value, int expireTime);

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
	public void hmset(String key, Map<String, Object> value, int expireTime);

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

	/* LIST *************************************************/

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
	 * start,end可为空，默认取全部
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
	 * start,end可为空，默认取全部
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public List<String> lrange(String key, long start, long end);

	/* SET */

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
	 * @param values
	 * @param expireTime
	 * @return 集合大小
	 */
	public long sadd(String key, int expireTime, Object... values);

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
	 * 删除多个set的值
	 * 
	 * @param key
	 * @param members
	 * @return SET长度
	 */
	public long srem(String key, Object... members);

	/**
	 * 检测值是否在set中
	 * 
	 * @param key
	 * @param member
	 * @return
	 */
	public boolean sismember(String key, Object member);

}