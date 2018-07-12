package cn.emay.store.redis.command.sortedset;

import cn.emay.store.redis.command.RedisCommand;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;

public class ZremerangeByRankCommand implements RedisCommand<Void> {

	private String key;

	private long start;

	private long end;

	public ZremerangeByRankCommand(String key, long start, long end) {
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
		command.zremrangeByRank(key, start, end);
		return null;
	}

}
