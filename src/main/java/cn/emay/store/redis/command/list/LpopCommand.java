package cn.emay.store.redis.command.list;

import java.io.UnsupportedEncodingException;

import cn.emay.json.JsonHelper;
import cn.emay.store.redis.command.RedisCommand;
import redis.clients.jedis.BinaryJedisClusterCommands;
import redis.clients.jedis.BinaryJedisCommands;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;

public class LpopCommand<K> implements RedisCommand<K> {

	private String key;

	private Class<K> clazz;

	private String datePattern;

	public LpopCommand(String key,  Class<K> clazz, String datePattern) {
		this.key = key;
		this.clazz = clazz;
		this.datePattern = datePattern;
	}

	@Override
	public K commond(Jedis client) {
		return this.exec(client, client, null);
	}

	@Override
	public K commond(JedisCluster client) {
		return this.exec(client, null, client);
	}
	
	@Override
	public K commond(ShardedJedis client) {
		return this.exec(client, client, null);
	}

	@SuppressWarnings("unchecked")
	private K exec(JedisCommands command, BinaryJedisCommands bjcommand, BinaryJedisClusterCommands bjccommand) {
		try {
			if (byte[].class.isAssignableFrom(clazz)) {
				byte[] value = null;
				if (bjcommand != null) {
					value = bjcommand.lpop(key.getBytes("UTF-8"));
				} else if (bjccommand != null) {
					value = bjccommand.lpop(key.getBytes("UTF-8"));
				}
				return (K) value;
			} else if (String.class.isAssignableFrom(clazz)) {
				return (K) command.lpop(key);
			} else {
				String value = command.lpop(key);
				return value == null ? null : JsonHelper.fromJson(clazz, value,datePattern);
			}
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
