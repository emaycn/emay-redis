package cn.emay.store.redis.command.impl.sortedset;

import cn.emay.store.redis.command.RedisCommand;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;

public class ZremerangeByScoreCommand implements RedisCommand<Void> {

	private String key;

	private double start;

	private double end;

	public ZremerangeByScoreCommand(String key, double start, double end) {
		this.key = key;
		this.start = start;
		this.end = end;
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
		command.zremrangeByScore(key, start, end);
		return null;
	}

}
