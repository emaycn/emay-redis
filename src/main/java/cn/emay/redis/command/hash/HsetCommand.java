package cn.emay.redis.command.hash;

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
public class HsetCommand implements RedisCommand<Boolean> {

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
		Long execResult = 0L;
		Long expireResult = 0L;
		try {
			if (byte[].class.isAssignableFrom(value.getClass())) {
				if (bjcommand != null) {
					execResult = bjcommand.hset(key.getBytes("UTF-8"), fieldname.getBytes("UTF-8"), (byte[]) value);
				} else if (bjccommand != null) {
					execResult = bjccommand.hset(key.getBytes("UTF-8"), fieldname.getBytes("UTF-8"), (byte[]) value);
				}
			} else if (String.class.isAssignableFrom(value.getClass())) {
				execResult = command.hset(key, fieldname, (String) value);
			} else {
				execResult = command.hset(key, fieldname, JsonHelper.toJsonString(value, datePattern));
			}
			if (expireTime > 0) {
				expireResult = command.expire(key, expireTime);
			}
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
		return execResult == 1 && expireResult == 1;
	}

}
