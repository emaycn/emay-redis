package cn.emay.store.redis.command.set;

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

public class SremCommand implements RedisCommand<Long> {

	private String key;

	private Object[] members;

	private String datePattern;

	public SremCommand(String key, String datePattern, Object... members) {
		this.key = key;
		this.members = members;
		this.datePattern = datePattern;
	}

	@Override
	public Long commond(Jedis client) {
		return this.exec(client, client, null);
	}

	@Override
	public Long commond(JedisCluster client) {
		return this.exec(client, null, client);
	}

	@Override
	public Long commond(ShardedJedis client) {
		return this.exec(client, client, null);
	}

	private Long exec(JedisCommands command, BinaryJedisCommands bjcommand, BinaryJedisClusterCommands bjccommand) {
		long length = 0;
		try {
			List<String> strvalues = new ArrayList<>();
			List<byte[]> bytvalues = new ArrayList<>();
			for (int i = 0; i < members.length; i++) {
				Object obj = members[i];
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
				length = command.srem(key, strvalues.toArray(new String[strvalues.size()]));
			}
			if (!bytvalues.isEmpty()) {
				if (bjcommand != null) {
					length = bjcommand.srem(key.getBytes("UTF-8"), bytvalues.toArray(new byte[bytvalues.size()][]));
				} else if (bjccommand != null) {
					length = bjccommand.srem(key.getBytes("UTF-8"), bytvalues.toArray(new byte[bytvalues.size()][]));
				}
			}
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
		return length;
	}

}
