package cn.emay.store.redis.command.sortedset;

import java.util.Map;

import cn.emay.store.redis.command.RedisCommand;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;

public class ZaddCommandAll implements RedisCommand<Void> {

	private String key;

	private Map<String, Double> scoreMembers;

	public ZaddCommandAll(String key, Map<String, Double> scoreMembers) {
		this.key = key;
		this.scoreMembers = scoreMembers;
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
		command.zadd(key, scoreMembers);
		return null;
	}

}
