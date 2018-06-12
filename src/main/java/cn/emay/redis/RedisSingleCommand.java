package cn.emay.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public abstract class RedisSingleCommand<T> {

	private JedisPool JedisPool;

	public RedisSingleCommand(JedisPool JedisPool) {
		this.JedisPool = JedisPool;
	}

	public abstract T commond(Jedis jedis);

	public T execute() {
		Jedis jedis = null;
		try {
			jedis = JedisPool.getResource();
			return this.commond(jedis);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

}
