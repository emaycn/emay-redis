package cn.emay.redis.command.hash;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import cn.emay.json.JsonHelper;
import cn.emay.redis.command.RedisCommand;
import redis.clients.jedis.BinaryJedisClusterCommands;
import redis.clients.jedis.BinaryJedisCommands;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;

/**
 * 
 * @author Frank
 *
 */
public class HgetAllCommand<K> implements RedisCommand<Map<String, K>> {

	private String key;

	private Class<K> clazz;

	private String datePattern;

	public HgetAllCommand(String key, Class<K> clazz, String datePattern) {
		this.key = key;
		this.clazz = clazz;
		this.datePattern = datePattern;
	}

	@Override
	public Map<String, K> commond(Jedis client) {
		return this.exec(client, client, null);
	}

	@Override
	public Map<String, K> commond(JedisCluster client) {
		return this.exec(client, null, client);
	}

	@Override
	public Map<String, K> commond(ShardedJedis client) {
		return this.exec(client, client, null);
	}

	@SuppressWarnings("unchecked")
	private Map<String, K> exec(JedisCommands command, BinaryJedisCommands bjcommand, BinaryJedisClusterCommands bjccommand) {
		try {
			if (byte[].class.isAssignableFrom(clazz)) {
				Map<byte[], byte[]> map = null;
				if (bjcommand != null) {
					map = bjcommand.hgetAll(key.getBytes("UTF-8"));
				} else if (bjccommand != null) {
					map = bjccommand.hgetAll(key.getBytes("UTF-8"));
				}
				if (map == null || map.isEmpty()) {
					return null;
				}
				Map<String, byte[]> map1 = new HashMap<>(map.size());
				for (Entry<byte[], byte[]> entry : map.entrySet()) {
					map1.put(new String(entry.getKey(), "UTF-8"), entry.getValue());
				}
				return (Map<String, K>) map1;
			} else if (String.class.isAssignableFrom(clazz)) {
				return (Map<String, K>) command.hgetAll(key);
			} else {
				Map<String, String> map = command.hgetAll(key);
				if (map == null || map.isEmpty()) {
					return null;
				}
				Map<String, K> map1 = new HashMap<>(map.size());
				for (Entry<String, String> entry : map.entrySet()) {
					map1.put(entry.getKey(), JsonHelper.fromJson(clazz, entry.getValue(), datePattern));
				}
				return map1;
			}
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
