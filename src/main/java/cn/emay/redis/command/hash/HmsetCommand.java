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
public class HmsetCommand implements RedisCommand<Boolean> {

	private String key;

	private Map<String, ?> value;

	private int expireTime;

	private String datePattern;

	public HmsetCommand(String key, Map<String, ?> value, int expireTime, String datePattern) {
		this.key = key;
		this.value = value;
		this.expireTime = expireTime;
		this.datePattern = datePattern;
	}

	@Override
	public Boolean commond(Jedis client) {
		return this.exec(client, client, null);
	}

	@Override
	public Boolean commond(JedisCluster client) {
		return this.exec(client, null, client);
	}

	@Override
	public Boolean commond(ShardedJedis client) {
		return this.exec(client, client, null);
	}

	private Boolean exec(JedisCommands command, BinaryJedisCommands bjcommand, BinaryJedisClusterCommands bjccommand) {
		String result = "ERROR";
		Long expireResult = 0L;
		try {
			int size = value.size();
			Map<byte[], byte[]> bytemap = new HashMap<>(size);
			Map<String, String> stringmap = new HashMap<>(size);
			for (Entry<String, ?> entry : value.entrySet()) {
				if (byte[].class.isAssignableFrom(entry.getValue().getClass())) {
					bytemap.put(entry.getKey().getBytes("UTF-8"), (byte[]) entry.getValue());
				} else if (String.class.isAssignableFrom(entry.getValue().getClass())) {
					stringmap.put(entry.getKey(), (String) entry.getValue());
				} else {
					stringmap.put(entry.getKey(), JsonHelper.toJsonStringWithoutNull(entry.getValue(), datePattern));
				}
			}
			if (!bytemap.isEmpty()) {
				if (bjcommand != null) {
					result = bjcommand.hmset(key.getBytes("UTF-8"), bytemap);
				} else if (bjccommand != null) {
					result = bjccommand.hmset(key.getBytes("UTF-8"), bytemap);
				}
			}
			if (!stringmap.isEmpty()) {
				result = command.hmset(key, stringmap);
			}
			if (expireTime > 0) {
				expireResult = command.expire(key, expireTime);
			}
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
		Boolean isOk = "OK".equalsIgnoreCase(result) && expireResult == 1L;
		return isOk;
	}

}
