package cn.emay.store.redis.command.sortedset;

import cn.emay.store.redis.command.RedisCommand;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;

public class ZrevrankCommand implements RedisCommand<Long> {

	private String key;
	
	private String member;

	public ZrevrankCommand(String key,String member) {
		this.key = key;
		this.member = member;
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
		return command.zrevrank(key, member);
	}

}
