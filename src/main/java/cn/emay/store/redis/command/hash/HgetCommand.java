package cn.emay.store.redis.command.hash;

import java.io.UnsupportedEncodingException;

import cn.emay.json.JsonHelper;
import cn.emay.store.redis.command.RedisCommand;
import redis.clients.jedis.BinaryJedisClusterCommands;
import redis.clients.jedis.BinaryJedisCommands;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;

public class HgetCommand<K> implements RedisCommand<K> {

	private String key;

	private Class<K> clazz;

	private String fieldname;

	private String datePattern;

	public HgetCommand(String key, String fieldname, Class<K> clazz, String datePattern) {
		this.key = key;
		this.clazz = clazz;
		this.fieldname = fieldname;
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
					value = bjcommand.hget(key.getBytes("UTF-8"), fieldname.getBytes("UTF-8"));
				} else if (bjccommand != null) {
					value = bjccommand.hget(key.getBytes("UTF-8"), fieldname.getBytes("UTF-8"));
				}
				return (K) value;
			} else if (String.class.isAssignableFrom(clazz)) {
				return (K) command.hget(key, fieldname);
			} else {
				String value = command.hget(key, fieldname);
				return value == null ? null : JsonHelper.fromJson(clazz, value, datePattern);
			}
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
