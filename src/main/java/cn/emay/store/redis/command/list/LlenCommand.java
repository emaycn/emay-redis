package cn.emay.store.redis.command.list;

import cn.emay.store.redis.command.RedisCommand;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;

public class LlenCommand implements RedisCommand<Long> {

	private String key;

	public LlenCommand(String key) {
		this.key = key;
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
		return command.llen(key);
	}

}
