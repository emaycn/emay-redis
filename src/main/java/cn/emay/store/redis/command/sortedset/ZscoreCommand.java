package cn.emay.store.redis.command.sortedset;

import cn.emay.store.redis.command.RedisCommand;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;

public class ZscoreCommand implements RedisCommand<Double> {

	private String key;
	
	private String member;

	public ZscoreCommand(String key,String member) {
		this.key = key;
		this.member = member;
	}

	@Override
	public Double commond(Jedis client) {
		return this.exec(client);
	}

	@Override
	public Double commond(JedisCluster client) {
		return this.exec(client);
	}

	@Override
	public Double commond(ShardedJedis client) {
		return this.exec(client);
	}

	private Double exec(JedisCommands command) {
		return command.zscore(key, member);
	}

}
