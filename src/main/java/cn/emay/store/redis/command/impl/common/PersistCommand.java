package cn.emay.store.redis.command.impl.common;

import cn.emay.store.redis.command.RedisCommand;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;

public class PersistCommand implements RedisCommand<Void> {

	private String key;

	public PersistCommand(String key) {
		this.key = key;
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

	private Void exec(JedisCommands command) {
		command.persist(key);
		return null;
	}

}
