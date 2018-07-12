package cn.emay.store.redis.command.impl.set;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cn.emay.json.JsonHelper;
import cn.emay.store.redis.command.RedisCommand;
import redis.clients.jedis.BinaryJedisClusterCommands;
import redis.clients.jedis.BinaryJedisCommands;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;

public class SrandmemberCommand<K> implements RedisCommand<List<K>> {

	private String key;

	private Class<K> clazz;

	private String datePattern;

	private int count;

	public SrandmemberCommand(String key, Class<K> clazz, int count, String datePattern) {
		this.key = key;
		this.clazz = clazz;
		this.count = count;
		this.datePattern = datePattern;
	}

	@Override
	public List<K> commond(Jedis client) {
		return this.exec(client, client, null);
	}

	@Override
	public List<K> commond(JedisCluster client) {
		return this.exec(client, null, client);
	}

	@Override
	public List<K> commond(ShardedJedis client) {
		return this.exec(client, client, null);
	}

	@SuppressWarnings("unchecked")
	private List<K> exec(JedisCommands command, BinaryJedisCommands bjcommand, BinaryJedisClusterCommands bjccommand) {
		try {
			if (byte[].class.isAssignableFrom(clazz)) {
				List<byte[]> value = null;
				if (bjcommand != null) {
					value = bjcommand.srandmember(key.getBytes("UTF-8"), count);
				} else if (bjccommand != null) {
					value = bjccommand.srandmember(key.getBytes("UTF-8"), count);
				}
				return (List<K>) value;
			} else if (String.class.isAssignableFrom(clazz)) {
				return (List<K>) command.srandmember(key, count);
			} else {
				List<String> value = command.srandmember(key, count);
				if (value == null || value.isEmpty()) {
					return null;
				}
				List<K> list = new ArrayList<>();
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
