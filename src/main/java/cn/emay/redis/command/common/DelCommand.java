package cn.emay.redis.command.common;

import cn.emay.redis.command.RedisCommand;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ShardedJedis;

/**
 * 删除命令
 * 
 * @author Frank
 *
 */
public class DelCommand implements RedisCommand<Long> {

	private String[] keys;

	public DelCommand(String... keys) {
		this.keys = keys;
	}

	@Override
	public Long commond(Jedis client) {
		return client.del(keys);
	}

	@Override
	public Long commond(JedisCluster client) {
		return client.del(keys);
	}

	@Override
	public Long commond(ShardedJedis client) {
		Long number = 0L;
		for (String key : keys) {
			number += client.del(key);
		}
		return number;
	}

}
