package cn.emay.store.redis.command.sortedset;

import cn.emay.store.redis.command.RedisCommand;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;

public class ZremCommand implements RedisCommand<Void> {

	private String key;

	private String[] members;

	public ZremCommand(String key, String... members) {
		this.key = key;
		this.members = members;
	}

	@Override
	public Void commond(Jedis client) {
		return this.exec(client);
	}

	@Override
	public Void commond(JedisCluster client) {
		return this.exec(client);
	}

	@Override
	public Void commond(ShardedJedis client) {
		return this.exec(client);
	}

	private Void exec(JedisCommands command) {
		command.zrem(key, members);
		return null;
	}

}
