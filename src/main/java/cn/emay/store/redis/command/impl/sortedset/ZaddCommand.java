package cn.emay.store.redis.command.impl.sortedset;

import cn.emay.store.redis.command.RedisCommand;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;

public class ZaddCommand implements RedisCommand<Void> {

	private String key;

	private String member;

	private double score;

	public ZaddCommand(String key, double score, String member) {
		this.key = key;
		this.member = member;
		this.score = score;
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
		command.zadd(key, score, member);
		return null;
	}

}
