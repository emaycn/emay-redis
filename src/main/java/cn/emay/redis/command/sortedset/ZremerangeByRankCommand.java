package cn.emay.redis.command.sortedset;

import cn.emay.redis.command.RedisCommand;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;

/**
 * 
 * @author Frank
 *
 */
public class ZremerangeByRankCommand implements RedisCommand<Long> {

	private String key;

	private long start;

	private long end;

	public ZremerangeByRankCommand(String key, long start, long end) {
		this.key = key;
		this.start = start;
		this.end = end;
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
		return command.zremrangeByRank(key, start, end);
	}

}
