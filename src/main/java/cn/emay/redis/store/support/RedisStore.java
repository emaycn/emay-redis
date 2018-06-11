package cn.emay.redis.store.support;

import java.io.UnsupportedEncodingException;

import cn.emay.redis.RedisClient;
import cn.emay.store.KeyValueStore;
import cn.emay.store.QueueStore;

public class RedisStore implements QueueStore, KeyValueStore {

	private int expireTime = -1;

	private RedisClient redis = null;

	public RedisStore() {

	}

	public RedisStore(RedisClient redis, int expireTime) {
		this.setExpireTime(expireTime);
		this.redis = redis;
	}

	@Override
	public Boolean exists(String name) {
		return redis.exists(name);
	}

	@Override
	public Boolean del(String name) {
		return redis.del(name);
	}

	@Override
	public Boolean set(String key, Object value) {
		return redis.set(key, value, expireTime);
	}

	@Override
	public Boolean set(String key, byte[] value) {
		try {
			return redis.setBytes(key.getBytes("UTF-8"), value, expireTime);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public <T> T get(String key, Class<T> clazz) {
		return redis.get(key, clazz);
	}

	@Override
	public String getString(String key) {
		return redis.get(key);
	}

	@Override
	public byte[] getBytes(String key) {
		try {
			return redis.getBytes(key.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public Boolean push(String queueName, Object object) {
		redis.lpush(queueName, -1, object);
		return true;
	}

	@Override
	public <T> T poll(String queueName, Class<T> clazz) {
		return redis.rpop(queueName, clazz);
	}

	@Override
	public Object poll(String queueName) {
		return redis.rpop(queueName);
	}

	@Override
	public Long size(String queueName) {
		return redis.llen(queueName);
	}

	public int getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(int expireTime) {
		this.expireTime = expireTime;
	}

	public RedisClient getRedis() {
		return redis;
	}

	public void setRedis(RedisClient redis) {
		this.redis = redis;
	}

}
