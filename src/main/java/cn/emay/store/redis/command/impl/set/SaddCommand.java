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

public class SaddCommand implements RedisCommand<Void> {

	private String key;

	private Object[] objects;

	private int expireTime;

	private String datePattern;

	public SaddCommand(String key, int expireTime, String datePattern, Object... objects) {
		this.key = key;
		this.objects = objects;
		this.expireTime = expireTime;
		this.datePattern = datePattern;
	}

	@Override
	public Void commond(Jedis client) {
		return this.exec(client, client, null);
	}

	@Override
	public Void commond(JedisCluster client) {
		return this.exec(client, null, client);
	}

	@Override
	public Void commond(ShardedJedis client) {
		return this.exec(client, client, null);
	}

	private Void exec(JedisCommands command, BinaryJedisCommands bjcommand, BinaryJedisClusterCommands bjccommand) {
		try {
			List<String> strvalues = new ArrayList<>();
			List<byte[]> bytvalues = new ArrayList<>();
			for (int i = 0; i < objects.length; i++) {
				Object obj = objects[i];
				if (obj == null) {
					continue;
				}
				if (byte[].class.isAssignableFrom(obj.getClass())) {
					bytvalues.add((byte[]) obj);
				} else if (String.class.isAssignableFrom(obj.getClass())) {
					strvalues.add((String) obj);
				} else {
					String value = JsonHelper.toJsonString(obj, datePattern);
					if (value != null) {
						strvalues.add(value);
					}
				}
			}
			if (!strvalues.isEmpty()) {
				command.sadd(key, strvalues.toArray(new String[strvalues.size()]));
			}
			if (!bytvalues.isEmpty()) {
				if (bjcommand != null) {
					bjcommand.sadd(key.getBytes("UTF-8"), bytvalues.toArray(new byte[bytvalues.size()][]));
				} else if (bjccommand != null) {
					bjccommand.sadd(key.getBytes("UTF-8"), bytvalues.toArray(new byte[bytvalues.size()][]));
				}
			}
			if (expireTime > 0) {
				command.expire(key, expireTime);
			}
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
		return null;
	}

}
