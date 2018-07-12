package cn.emay.store.redis.command.impl.sortedset;

import java.util.Set;

import cn.emay.store.redis.command.RedisCommand;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;

public class ZrevrangeCommand implements RedisCommand<Set<String>> {

	private String key;
	
	private long start;
	
	private long end;

	public ZrevrangeCommand(String key,long start,long end) {
		this.key = key;
		this.start = start;
		this.end = end;
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
		return command.zrevrange(key, start, end);
	}

}
