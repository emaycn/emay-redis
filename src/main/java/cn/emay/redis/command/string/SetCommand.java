package cn.emay.redis.command.string;

import java.io.UnsupportedEncodingException;

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
public class SetCommand implements RedisCommand<Boolean> {

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
		String setResult = null;
		if (byte[].class.isAssignableFrom(value.getClass())) {
			byte[] valuebytes = (byte[]) value;
			try {
				if (expireTime > 0) {
					if (bjcommand != null) {
						setResult = bjcommand.setex(key.getBytes("UTF-8"), expireTime, valuebytes);
					} else if (bjccommand != null) {
						setResult = bjccommand.setex(key.getBytes("UTF-8"), expireTime, valuebytes);
					}
				} else {
					if (bjcommand != null) {
						setResult = bjcommand.set(key.getBytes("UTF-8"), valuebytes);
					} else if (bjccommand != null) {
						setResult = bjccommand.set(key.getBytes("UTF-8"), valuebytes);
					}
				}
			} catch (UnsupportedEncodingException e) {
				throw new IllegalArgumentException(e);
			}
		} else if (String.class.isAssignableFrom(value.getClass())) {
			if (expireTime > 0) {
				setResult = command.setex(key, expireTime, (String) value);
			} else {
				setResult = command.set(key, (String) value);
			}
		} else {
			if (expireTime > 0) {
				setResult = command.setex(key, expireTime, JsonHelper.toJsonString(value, datePattern));
			} else {
				setResult = command.set(key, JsonHelper.toJsonString(value, datePattern));
			}
		}
		return "OK".equalsIgnoreCase(setResult);
	}

}
