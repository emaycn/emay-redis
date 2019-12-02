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
public class SetnxCommand implements RedisCommand<Boolean> {

	private String key;

	private Object value;

	private int expireTime;

	private String datePattern;

	public SetnxCommand(String key, Object value, int expireTime, String datePattern) {
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

	private Boolean exec(JedisCommands command, BinaryJedisCommands bjCommand, BinaryJedisClusterCommands bjcCommand) {
		long number = 0;
		if (byte[].class.isAssignableFrom(value.getClass())) {
			byte[] valuebytes = (byte[]) value;
			try {
				if (bjCommand != null) {
					number = bjCommand.setnx(key.getBytes("UTF-8"), valuebytes);
				} else if (bjcCommand != null) {
					number = bjcCommand.setnx(key.getBytes("UTF-8"), valuebytes);
				}
			} catch (UnsupportedEncodingException e) {
				throw new IllegalArgumentException(e);
			}
		} else if (String.class.isAssignableFrom(value.getClass())) {
			number = command.setnx(key, (String) value);
		} else {
			number = command.setnx(key, JsonHelper.toJsonStringWithoutNull(value, datePattern));
		}
		if (number != 0) {
			if (expireTime > 0) {
				command.expire(key, expireTime);
			}
			return true;
		} else {
			return false;
		}
	}

}
