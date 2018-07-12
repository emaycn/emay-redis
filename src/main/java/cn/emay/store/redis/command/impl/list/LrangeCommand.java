package cn.emay.store.redis.command.impl.list;

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

public class LrangeCommand<K> implements RedisCommand<List<K>> {

	private String key;

	private Class<K> clazz;

	private String datePattern;

	private long start;

	private long end;

	public LrangeCommand(String key, long start, long end, Class<K> clazz, String datePattern) {
		this.key = key;
		this.clazz = clazz;
		this.start = start;
		this.end = end;
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
			long startnew = (start <= 0) ? 0l : start;
			long endnew = (end < 0) ? -1l : end;
			List<K> lo = null;
			if (byte[].class.isAssignableFrom(clazz)) {
				if (bjcommand != null) {
					lo = (List<K>) bjcommand.lrange(key.getBytes("UTF-8"), startnew, endnew);
				} else if (bjccommand != null) {
					lo = (List<K>) bjccommand.lrange(key.getBytes("UTF-8"), startnew, endnew);
				}
			} else if (String.class.isAssignableFrom(clazz)) {
				lo = (List<K>) command.lrange(key, startnew, endnew);
			} else {
				List<String> list = command.lrange(key, startnew, endnew);
				if (list == null || list.isEmpty()) {
					return null;
				}
				lo = new ArrayList<>(list.size());
				for (String value : list) {
					K t = JsonHelper.fromJson(clazz, value, datePattern);
					if (t != null) {
						lo.add(t);
					}
				}
			}
			return lo;
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
