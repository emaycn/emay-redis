package cn.emay.store.redis.command.impl.hash;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import cn.emay.json.JsonHelper;
import cn.emay.store.redis.command.RedisCommand;
import redis.clients.jedis.BinaryJedisClusterCommands;
import redis.clients.jedis.BinaryJedisCommands;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;

public class HmsetCommand implements RedisCommand<Void> {

	private String key;

	private Map<String, Object> value;

	private int expireTime;

	private String datePattern;

	public HmsetCommand(String key, Map<String, Object> value, int expireTime, String datePattern) {
		this.key = key;
		this.value = value;
		this.expireTime = expireTime;
		this.datePattern = datePattern;
	}

	@Override
	public Void commond(Jedis client) {
		this.exec(client, client, null);
		return null;
	}

	@Override
	public Void commond(JedisCluster client) {
		this.exec(client, null, client);
		return null;
	}
	
	@Override
	public Void commond(ShardedJedis client) {
		this.exec(client, client, null);
		return null;
	}

	private void exec(JedisCommands command, BinaryJedisCommands bjcommand, BinaryJedisClusterCommands bjccommand) {
		try {
			Map<byte[], byte[]> bytemap = new HashMap<>();
			Map<String, String> stringmap = new HashMap<>();
			for (Entry<String, Object> entry : value.entrySet()) {
				if (byte[].class.isAssignableFrom(entry.getValue().getClass())) {
					bytemap.put(entry.getKey().getBytes("UTF-8"), (byte[]) entry.getValue());
				} else if (String.class.isAssignableFrom(entry.getValue().getClass())) {
					stringmap.put(entry.getKey(), (String) entry.getValue());
				} else {
					stringmap.put(entry.getKey(), JsonHelper.toJsonString(entry.getValue(), datePattern));
				}
			}
			if (!bytemap.isEmpty()) {
				if (bjcommand != null) {
					bjcommand.hmset(key.getBytes("UTF-8"), bytemap);
				} else if (bjccommand != null) {
					bjccommand.hmset(key.getBytes("UTF-8"), bytemap);
				}
			}
			if (!stringmap.isEmpty()) {
				command.hmset(key, stringmap);
			}
			if (expireTime > 0) {
				command.expire(key, expireTime);
			}
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
