package cn.emay.store.redis.command.hash;

import cn.emay.store.redis.command.RedisCommand;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;

public class HincrByCommand implements RedisCommand<Long> {

	private String key;

	private long number;

	private String fieldname;

	public HincrByCommand(String key, String fieldname, long number) {
		this.key = key;
		this.number = number;
		this.fieldname = fieldname;
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
		return command.hincrBy(key, fieldname, number);
	}

}
