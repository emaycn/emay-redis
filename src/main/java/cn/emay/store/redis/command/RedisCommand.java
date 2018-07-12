package cn.emay.store.redis.command;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ShardedJedis;

/**
 * 执行器
 * 
 * @author Frank
 *
 * @param <T>
 */
public interface RedisCommand<T> {

	/**
	 * 单点redis执行方法
	 * 
	 * @param client
	 * @return
	 */
	T commond(Jedis client);

	/**
	 * 集群redis执行方法
	 * 
	 * @param client
	 * @return
	 */
	T commond(JedisCluster client);

	/**
	 * 分片redis执行方法
	 * 
	 * @param client
	 * @return
	 */
	T commond(ShardedJedis client);

}
