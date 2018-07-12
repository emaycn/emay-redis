package cn.emay.store.redis.command.string;

import java.io.UnsupportedEncodingException;

import cn.emay.json.JsonHelper;
import cn.emay.store.redis.command.RedisCommand;
import redis.clients.jedis.BinaryJedisClusterCommands;
import redis.clients.jedis.BinaryJedisCommands;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;

public class SetCommand implements RedisCommand<Void> {

	private String key;

	private Object value;

	private int expireTime;

	private String datePattern;

	public SetCommand(String key, Object value, int expireTime, String datePattern) {
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
		if (byte[].class.isAssignableFrom(value.getClass())) {
			byte[] valuebytes = (byte[]) value;
			try {
				if (expireTime > 0) {
					if (bjcommand != null) {
						bjcommand.setex(key.getBytes("UTF-8"), expireTime, valuebytes);
					}else if (bjccommand != null) {
						bjccommand.setex(key.getBytes("UTF-8"), expireTime, valuebytes);
					}
				} else {
					if (bjcommand != null) {
						bjcommand.set(key.getBytes("UTF-8"), valuebytes);
					}else if (bjccommand != null) {
						bjccommand.set(key.getBytes("UTF-8"), valuebytes);
					}
				}
			} catch (UnsupportedEncodingException e) {
				throw new IllegalArgumentException(e);
			}
		} else if (String.class.isAssignableFrom(value.getClass())) {
			if (expireTime > 0) {
				command.setex(key, expireTime, (String) value);
			} else {
				command.set(key, (String) value);
			}
		} else {
			if (expireTime > 0) {
				command.setex(key, expireTime, JsonHelper.toJsonString(value, datePattern));
			} else {
				command.set(key, JsonHelper.toJsonString(value, datePattern));
			}
		}
	}

}
