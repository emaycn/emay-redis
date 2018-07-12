package cn.emay.store.redis.command.impl.common;

import cn.emay.store.redis.command.RedisCommand;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;

public class ExistsCommand implements RedisCommand<Boolean> {

	private String key;

	public ExistsCommand(String key) {
		this.key = key;
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
		return command.exists(key);
	}

	

}
