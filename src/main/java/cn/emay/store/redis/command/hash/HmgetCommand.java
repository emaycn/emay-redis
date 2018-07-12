package cn.emay.store.redis.command.hash;

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

public class HmgetCommand<K> implements RedisCommand<List<K>> {

	private String key;

	private Class<K> clazz;

	private String[] fieldnames;

	private String datePattern;

	public HmgetCommand(String key, Class<K> clazz, String datePattern, String... fieldnames) {
		this.key = key;
		this.clazz = clazz;
		this.fieldnames = fieldnames;
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
				byte[][] bytes = new byte[fieldnames.length][];
				for (int i = 0; i < fieldnames.length; i++) {
					bytes[i] = fieldnames[i].getBytes("UTF-8");
				}
				List<byte[]> value = null;
				if (bjcommand != null) {
					value = bjcommand.hmget(key.getBytes("UTF-8"), bytes);
				} else if (bjccommand != null) {
					value = bjccommand.hmget(key.getBytes("UTF-8"), bytes);
				}
				return (List<K>) value;
			} else if (String.class.isAssignableFrom(clazz)) {
				return (List<K>) command.hmget(key, fieldnames);
			} else {
				List<String> stringlist1 = command.hmget(key, fieldnames);
				if (stringlist1 == null || stringlist1.isEmpty()) {
					return null;
				}
				List<K> stringlist = new ArrayList<>(stringlist1.size());
				for (String sr : stringlist1) {
					stringlist.add(JsonHelper.fromJson(clazz, sr, datePattern));
				}
				return stringlist;
			}
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
