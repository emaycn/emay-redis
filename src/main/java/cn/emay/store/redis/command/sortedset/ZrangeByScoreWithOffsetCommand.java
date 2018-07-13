package cn.emay.store.redis.command.sortedset;

import java.util.Set;

import cn.emay.store.redis.command.RedisCommand;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.Tuple;

public class ZrangeByScoreWithOffsetCommand implements RedisCommand<Set<Tuple>> {

	private String key;

	private double min;

	private double max;

	private int offset;

	private int count;

	public ZrangeByScoreWithOffsetCommand(String key, double min, double max, int offset, int count) {
		this.key = key;
		this.min = min;
		this.max = max;
		this.offset = offset;
		this.count = count;
	}

	@Override
	public Set<Tuple> commond(Jedis client) {
		return this.exec(client);
	}

	@Override
	public Set<Tuple> commond(JedisCluster client) {
		return this.exec(client);
	}

	@Override
	public Set<Tuple> commond(ShardedJedis client) {
		return this.exec(client);
	}

	private Set<Tuple> exec(JedisCommands command) {
		return command.zrangeByScoreWithScores(key, min, max, offset, count);
	}

}
