package cn.emay.store.redis.command.common;

import cn.emay.store.redis.command.RedisCommand;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;

public class ExpireCommand implements RedisCommand<Void> {

	private String key;

	private int seconds;

	public ExpireCommand(String key, int seconds) {
		this.key = key;
		this.seconds = seconds;
	}

	@Override
	public Void commond(Jedis client) {
		this.exec(client);
		return null;
	}

	@Override
	public Void commond(JedisCluster client) {
		this.exec(client);
		return null;
	}

	@Override
	public Void commond(ShardedJedis client) {
		this.exec(client);
		return null;
	}

	private void exec(JedisCommands command) {
		command.expire(key, seconds);
	}

}
