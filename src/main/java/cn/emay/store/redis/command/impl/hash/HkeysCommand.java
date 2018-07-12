package cn.emay.store.redis.command.impl.hash;

import java.util.Set;

import cn.emay.store.redis.command.RedisCommand;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;

public class HkeysCommand implements RedisCommand<Set<String>> {

	private String key;

	public HkeysCommand(String key) {
		this.key = key;
	}

	@Override
	public Set<String> commond(Jedis client) {
		return this.exec(client);
	}

	@Override
	public Set<String> commond(JedisCluster client) {
		return this.exec(client);
	}
	
	@Override
	public Set<String> commond(ShardedJedis client) {
		return this.exec(client);
	}

	private Set<String> exec(JedisCommands command) {
		return command.hkeys(key);
	}

}
