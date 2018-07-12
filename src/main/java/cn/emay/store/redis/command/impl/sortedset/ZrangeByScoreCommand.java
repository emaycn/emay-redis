package cn.emay.store.redis.command.impl.sortedset;

import java.util.Set;

import cn.emay.store.redis.command.RedisCommand;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.Tuple;

public class ZrangeByScoreCommand implements RedisCommand<Set<Tuple>> {

	private String key;
	
	private double min;
	
	private double max;

	public ZrangeByScoreCommand(String key,double min,double max) {
		this.key = key;
		this.min = min;
		this.max = max;
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
		return command.zrangeByScoreWithScores(key, min, max);
	}

}
