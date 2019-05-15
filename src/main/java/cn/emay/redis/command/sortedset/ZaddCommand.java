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
public class ZaddCommand implements RedisCommand<Boolean> {

	private String key;

	private String member;

	private double score;

	public ZaddCommand(String key, double score, String member) {
		this.key = key;
		this.member = member;
		this.score = score;
	}

	@Override
	public Boolean commond(Jedis client) {
		return this.exec(client);
	}

	@Override
	public Boolean commond(JedisCluster client) {
		return this.exec(client);
	}

	@Override
	public Boolean commond(ShardedJedis client) {
		return this.exec(client);
	}

	private Boolean exec(JedisCommands command) {
		return command.zadd(key, score, member) == 1;
	}

}
