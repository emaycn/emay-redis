package cn.emay.redis.command.hash;

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
public class HexistsCommand implements RedisCommand<Boolean> {

	private String key;

	private String fieldname;

	public HexistsCommand(String key, String fieldname) {
		this.fieldname = fieldname;
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
		return command.hexists(key, fieldname);
	}

}
