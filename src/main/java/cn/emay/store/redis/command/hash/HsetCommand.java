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

public class HsetCommand implements RedisCommand<Void> {

	private String key;

	private String fieldname;

	private Object value;

	private int expireTime;

	private String datePattern;

	public HsetCommand(String key, String fieldname, Object value, int expireTime, String datePattern) {
		this.key = key;
		this.value = value;
		this.fieldname = fieldname;
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
			if (byte[].class.isAssignableFrom(value.getClass())) {
				if (bjcommand != null) {
					bjcommand.hset(key.getBytes("UTF-8"), fieldname.getBytes("UTF-8"), (byte[]) value);
				}else if (bjccommand != null) {
					bjccommand.hset(key.getBytes("UTF-8"), fieldname.getBytes("UTF-8"), (byte[]) value);
				}
			} else if (String.class.isAssignableFrom(value.getClass())) {
				command.hset(key, fieldname, (String) value);
			} else {
				command.hset(key, fieldname, JsonHelper.toJsonString(value, datePattern));
			}
			if (expireTime > 0) {
				command.expire(key, expireTime);
			}
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
