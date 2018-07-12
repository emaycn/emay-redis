package cn.emay.store.redis.command.impl.set;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

import cn.emay.json.JsonHelper;
import cn.emay.store.redis.command.RedisCommand;
import redis.clients.jedis.BinaryJedisClusterCommands;
import redis.clients.jedis.BinaryJedisCommands;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;

public class SmembersCommand<K> implements RedisCommand<Set<K>> {

	private String key;

	private Class<K> clazz;

	private String datePattern;

	public SmembersCommand(String key, Class<K> clazz, String datePattern) {
		this.key = key;
		this.clazz = clazz;
		this.datePattern = datePattern;
	}

	@Override
	public Set<K> commond(Jedis client) {
		return this.exec(client, client, null);
	}

	@Override
	public Set<K> commond(JedisCluster client) {
		return this.exec(client, null, client);
	}

	@Override
	public Set<K> commond(ShardedJedis client) {
		return this.exec(client, client, null);
	}

	@SuppressWarnings("unchecked")
	private Set<K> exec(JedisCommands command, BinaryJedisCommands bjcommand, BinaryJedisClusterCommands bjccommand) {
		try {
			if (byte[].class.isAssignableFrom(clazz)) {
				Set<byte[]> value = null;
				if (bjcommand != null) {
					value = bjcommand.smembers(key.getBytes("UTF-8"));
				} else if (bjccommand != null) {
					value = bjccommand.smembers(key.getBytes("UTF-8"));
				}
				return (Set<K>) value;
			} else if (String.class.isAssignableFrom(clazz)) {
				return (Set<K>) command.smembers(key);
			} else {
				Set<String> value = command.smembers(key);
				if (value == null || value.isEmpty()) {
					return null;
				}
				Set<K> list = new HashSet<>();
				for (String va : value) {
					K json = JsonHelper.fromJson(clazz, va, datePattern);
					if (json != null) {
						list.add(json);
					}
				}
				return list;
			}
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
