package cn.emay.store.redis.command.impl.sortedset;

import cn.emay.store.redis.command.RedisCommand;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;

public class ZcountCommand implements RedisCommand<Long> {

	private String key;

	private double min;
	
	private double max;

	public ZcountCommand(String key, double min, double max) {
		this.key = key;
		this.max = max;
		this.min = min;
	}

	@Override
	public Long commond(Jedis client) {
		return this.exec(client);
	}

	@Override
	public Long commond(JedisCluster client) {
		return this.exec(client);
	}

	@Override
	public Long commond(ShardedJedis client) {
		return this.exec(client);
	}

	private Long exec(JedisCommands command) {
		return command.zcount(key, min, max);
	}

}
