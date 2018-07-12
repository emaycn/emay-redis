package cn.emay.store.redis.command.hash;

import cn.emay.store.redis.command.RedisCommand;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;

public class HdelCommand implements RedisCommand<Void> {

	private String key;

	private String[] fieldname;

	public HdelCommand(String key, String... fieldname) {
		this.key = key;
		this.fieldname = fieldname;
	}

	@Override
	public Void commond(Jedis client) {
		this.exec(client);
		return null;
	}

	@Override
	public Void commond(JedisCluster client) {
		this.exec(client);
		return null;
	}

	@Override
	public Void commond(ShardedJedis client) {
		this.exec(client);
		return null;
	}

	private void exec(JedisCommands command) {
		command.hdel(key, fieldname);
	}

}
