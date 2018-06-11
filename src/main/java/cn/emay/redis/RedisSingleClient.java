package cn.emay.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import cn.emay.json.JsonHelper;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisSingleClient implements RedisClient {

	private JedisPool JedisPool;

	private Properties properties;

	public RedisSingleClient(String host, int port, int timeout, int maxIdle, int maxTotal, int minIdle, long maxWaitMillis) {
		this(host, port, timeout, maxIdle, maxTotal, minIdle, maxWaitMillis, null, null);
	}

	public RedisSingleClient(String host, int port, int timeout, int maxIdle, int maxTotal, int minIdle, long maxWaitMillis, String password) {
		this(host, port, timeout, maxIdle, maxTotal, minIdle, maxWaitMillis, password, null);
	}

	private RedisSingleClient(String host, int port, int timeout, int maxIdle, int maxTotal, int minIdle, long maxWaitMillis, String password, String datePattern) {
		properties = new Properties();
		properties.setProperty("host", host);
		properties.setProperty("port", String.valueOf(port));
		properties.setProperty("timeout", String.valueOf(timeout));
		properties.setProperty("maxIdle", String.valueOf(maxIdle));
		properties.setProperty("maxTotal", String.valueOf(maxTotal));
		properties.setProperty("minIdle", String.valueOf(minIdle));
		properties.setProperty("maxWaitMillis", String.valueOf(maxWaitMillis));
		if (password != null) {
			properties.setProperty("password", password);
		}
		properties.setProperty("datePattern", datePattern == null ? "yyyy-MM-dd HH:mm:ss" : datePattern);
		this.init();
	}

	public RedisSingleClient() {

	}

	public void init() {
		String datePattern = properties.getProperty("datePattern");
		properties.setProperty("datePattern", datePattern == null ? "yyyy-MM-dd HH:mm:ss" : datePattern);
		String host = properties.getProperty("host");
		int port = Integer.valueOf(properties.getProperty("port"));
		int timeout = Integer.valueOf(properties.getProperty("timeout"));
		String password = properties.getProperty("password");
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxIdle(Integer.valueOf(properties.getProperty("maxIdle")));
		poolConfig.setMaxTotal(Integer.valueOf(properties.getProperty("maxTotal")));
		poolConfig.setMaxWaitMillis(Long.valueOf(properties.getProperty("maxWaitMillis")));
		poolConfig.setMinIdle(Integer.valueOf(properties.getProperty("minIdle")));
		if (password == null) {
			JedisPool = new JedisPool(poolConfig, host, port, timeout);
		} else {
			JedisPool = new JedisPool(poolConfig, host, port, timeout, password);
		}
	}

	private String toJsonString(Object obj) {
		return JsonHelper.toJsonString(obj, properties.getProperty("datePattern"));
	}

	private <T> T fromJson(Class<T> clazz, String jsonString) {
		return JsonHelper.fromJson(clazz, jsonString, properties.getProperty("datePattern"));
	}

	public void close() {
		JedisPool.close();
	}

	@Override
	public Boolean exists(final String key) {
		return new RedisSingleCommand<Boolean>(JedisPool) {
			public Boolean commond(Jedis jedis) {
				return jedis.exists(key);
			}
		}.execute();
	}

	@Override
	public Boolean del(final String... keys) {
		return new RedisSingleCommand<Boolean>(JedisPool) {
			public Boolean commond(Jedis jedis) {
				jedis.del(keys);
				return true;
			}
		}.execute();
	}

	@Override
	public Boolean expire(final String key, final int seconds) {
		return new RedisSingleCommand<Boolean>(JedisPool) {
			public Boolean commond(Jedis jedis) {
				jedis.expire(key, seconds);
				return true;
			}
		}.execute();
	}

	@Override
	public Boolean set(final String key, final Object value, final int expireTime) {
		return new RedisSingleCommand<Boolean>(JedisPool) {
			public Boolean commond(Jedis jedis) {
				String valueJson = null;
				if (!value.getClass().equals(String.class)) {
					valueJson = toJsonString(value);
				} else {
					valueJson = (String) value;
				}
				jedis.set(key, valueJson);
				if (expireTime > 0) {
					jedis.expire(key, expireTime);
				}
				return true;
			}
		}.execute();
	}

	@Override
	public String get(final String key) {
		return new RedisSingleCommand<String>(JedisPool) {
			public String commond(Jedis jedis) {
				return jedis.get(key);
			}
		}.execute();
	}

	@Override
	public Object getObject(final String key) {
		return new RedisSingleCommand<String>(JedisPool) {
			public String commond(Jedis jedis) {
				return jedis.get(key);
			}
		}.execute();
	}

	@Override
	public <T> T get(String key, Class<T> clazz) {
		String value = this.get(key);
		if (value == null) {
			return null;
		}
		return fromJson(clazz, value);
	}

	@Override
	public Boolean hdel(final String key, final String... fieldnames) {
		return new RedisSingleCommand<Boolean>(JedisPool) {
			public Boolean commond(Jedis jedis) {
				jedis.hdel(key, fieldnames);
				return true;
			}
		}.execute();
	}

	@Override
	public Boolean hmset(final String key, final Map<String, Object> value, final int expireTime) {
		return new RedisSingleCommand<Boolean>(JedisPool) {
			public Boolean commond(Jedis jedis) {
				Map<String, String> jsonMap = new HashMap<String, String>();
				if (value == null || value.size() == 0) {
					return false;
				}
				for (Entry<String, Object> entry : value.entrySet()) {
					String valueJson = null;
					if (!entry.getValue().getClass().equals(String.class)) {
						valueJson = toJsonString(entry.getValue());
					} else {
						valueJson = (String) entry.getValue();
					}
					jsonMap.put(entry.getKey(), valueJson);
				}
				jedis.hmset(key, jsonMap);
				if (expireTime > 0) {
					jedis.expire(key, expireTime);
				}
				return true;
			}
		}.execute();
	}

	@Override
	public Boolean hset(final String key, final String fieldname, final Object value, final int expireTime) {
		return new RedisSingleCommand<Boolean>(JedisPool) {
			public Boolean commond(Jedis jedis) {
				String valueJson = null;
				if (!value.getClass().equals(String.class)) {
					valueJson = toJsonString(value);
				} else {
					valueJson = (String) value;
				}
				jedis.hset(key, fieldname, valueJson);
				if (expireTime > 0) {
					jedis.expire(key, expireTime);
				}
				return true;
			}
		}.execute();
	}

	@Override
	public Long hlen(final String key) {
		return new RedisSingleCommand<Long>(JedisPool) {
			public Long commond(Jedis jedis) {
				return jedis.hlen(key);
			}
		}.execute();
	}

	@Override
	public <T> T hget(String key, String fieldname, Class<T> clazz) {
		String value = this.hget(key, fieldname);
		if (value == null) {
			return null;
		}
		return fromJson(clazz, value);
	}

	@Override
	public String hget(final String key, final String fieldname) {
		return new RedisSingleCommand<String>(JedisPool) {
			public String commond(Jedis jedis) {
				return jedis.hget(key, fieldname);
			}
		}.execute();
	}

	@Override
	public <T> List<T> hmget(String key, Class<T> clazz, String... fieldnames) {
		List<String> list = this.hmget(key, fieldnames);
		if (list == null || list.size() == 0) {
			return null;
		}
		List<T> result = new ArrayList<T>();
		for (String json : list) {
			result.add(fromJson(clazz, json));
		}
		return result;
	}

	@Override
	public List<String> hmget(final String key, final String... fieldnames) {
		return new RedisSingleCommand<List<String>>(JedisPool) {
			public List<String> commond(Jedis jedis) {
				return jedis.hmget(key, fieldnames);
			}
		}.execute();
	}

	@Override
	public <T> Map<String, T> hgetAll(String key, Class<T> clazz) {
		Map<String, String> map = this.hgetAll(key);
		if (map == null || map.size() == 0) {
			return null;
		}
		Map<String, T> objmap = new HashMap<String, T>();
		for (Entry<String, String> entry : map.entrySet()) {
			T boj = fromJson(clazz, entry.getValue());
			objmap.put(entry.getKey(), boj);
		}
		return objmap;
	}

	@Override
	public Map<String, String> hgetAll(final String key) {
		return new RedisSingleCommand<Map<String, String>>(JedisPool) {
			public Map<String, String> commond(Jedis jedis) {
				return jedis.hgetAll(key);
			}
		}.execute();
	}

	@Override
	public Boolean hexists(final String key, final String fieldname) {
		return new RedisSingleCommand<Boolean>(JedisPool) {
			public Boolean commond(Jedis jedis) {
				return jedis.hexists(key, fieldname);
			}
		}.execute();
	}

	@Override
	public Set<String> hkeys(final String key) {
		return new RedisSingleCommand<Set<String>>(JedisPool) {
			public Set<String> commond(Jedis jedis) {
				return jedis.hkeys(key);
			}
		}.execute();
	}

	@Override
	public Boolean lpush(String key, Object object, int expireTime) {
		return this.listpush(key, expireTime, true, object);
	}

	@Override
	public Long lpush(String key, int expireTime, Object object) {
		return this.listlpush(key, expireTime, true, object);
	}

	@Override
	public Boolean rpush(String key, Object object, int expireTime) {
		return this.listpush(key, expireTime, false, object);
	}

	@Override
	public Boolean lpush(String key, int expireTime, Object... objects) {
		return this.listpush(key, expireTime, true, objects);
	}

	@Override
	public Boolean rpush(String key, int expireTime, Object... objects) {
		return this.listpush(key, expireTime, false, objects);
	}

	private Boolean listpush(final String key, final int expireTime, final boolean isLeft, final Object... objects) {
		return new RedisSingleCommand<Boolean>(JedisPool) {
			public Boolean commond(Jedis jedis) {
				String[] values = new String[objects.length];
				for (int i = 0; i < objects.length; i++) {
					if (objects[i].getClass().equals(String.class)) {
						values[i] = (String) objects[i];
					} else {
						values[i] = toJsonString(objects[i]);
					}
				}
				if (isLeft) {
					jedis.lpush(key, values);
				} else {
					jedis.rpush(key, values);
				}
				if (expireTime > 0) {
					jedis.expire(key, expireTime);
				}
				return true;
			}
		}.execute();

	}

	private Long listlpush(final String key, final int expireTime, final boolean isLeft, final Object... objects) {
		return new RedisSingleCommand<Long>(JedisPool) {
			public Long commond(Jedis jedis) {
				String[] values = new String[objects.length];
				Long result = 0l;
				for (int i = 0; i < objects.length; i++) {
					if (objects[i].getClass().equals(String.class)) {
						values[i] = (String) objects[i];
					} else {
						values[i] = toJsonString(objects[i]);
					}
				}
				if (isLeft) {
					result = jedis.lpush(key, values);
				} else {
					result = jedis.rpush(key, values);
				}
				if (expireTime > 0) {
					jedis.expire(key, expireTime);
				}
				return result;
			}
		}.execute();

	}

	@Override
	public <T> T lpop(String key, Class<T> clazz) {
		String value = this.listPop(key, false, 0, true);
		T t = null;
		if (value != null) {
			t = fromJson(clazz, value);
		}
		return t;
	}

	@Override
	public String lpop(String key) {
		return this.listPop(key, false, 0, true);
	}

	@Override
	public <T> T blpop(String key, int blockingtimeout, Class<T> clazz) {
		String value = this.listPop(key, true, blockingtimeout < 0 ? 0 : blockingtimeout, true);
		T t = null;
		if (value != null) {
			t = fromJson(clazz, value);
		}
		return t;
	}

	@Override
	public String blpop(String key, int blockingtimeout) {
		return this.listPop(key, true, blockingtimeout < 0 ? 0 : blockingtimeout, true);
	}

	@Override
	public <T> T rpop(String key, Class<T> clazz) {
		String value = this.listPop(key, false, 0, false);
		T t = null;
		if (value != null) {
			t = fromJson(clazz, value);
		}
		return t;
	}

	@Override
	public String rpop(String key) {
		return this.listPop(key, false, 0, false);
	}

	@Override
	public <T> T brpop(String key, int blockingtimeout, Class<T> clazz) {
		String value = this.listPop(key, true, blockingtimeout < 0 ? 0 : blockingtimeout, false);
		T t = null;
		if (value != null) {
			t = fromJson(clazz, value);
		}
		return t;
	}

	@Override
	public String brpop(String key, int blockingtimeout) {
		return this.listPop(key, true, blockingtimeout < 0 ? 0 : blockingtimeout, false);
	}

	private String listPop(final String key, final boolean isBlocking, final int blockingtimeout, final boolean isLeft) {
		return new RedisSingleCommand<String>(JedisPool) {
			public String commond(Jedis jedis) {
				String value = null;
				if (isBlocking) {
					List<String> result = null;
					if (isLeft) {
						result = jedis.blpop(blockingtimeout, key);
					} else {
						result = jedis.brpop(blockingtimeout, key);
					}
					if (result != null && result.size() > 1) {
						value = result.get(1);
					}
				} else {
					if (isLeft) {
						value = jedis.lpop(key);
					} else {
						value = jedis.rpop(key);
					}
				}
				return value;
			}
		}.execute();
	}

	@Override
	public <T> List<T> lrange(String key, long start, long end, Class<T> clazz) {
		List<String> list = this.lrange(key, start, end);
		if (list == null || list.size() == 0) {
			return null;
		}
		List<T> lo = new ArrayList<T>();
		for (String value : list) {
			T t = fromJson(clazz, value);
			lo.add(t);
		}
		return lo;
	}

	@Override
	public List<String> lrange(final String key, final Long start, final Long end) {
		return new RedisSingleCommand<List<String>>(JedisPool) {
			public List<String> commond(Jedis jedis) {
				return jedis.lrange(key, (start == null || start <= 0) ? 0l : start, (end == null || end < 0) ? -1l : end);
			}
		}.execute();
	}

	@Override
	public Long llen(final String key) {
		return new RedisSingleCommand<Long>(JedisPool) {
			public Long commond(Jedis jedis) {
				return jedis.llen(key);
			}
		}.execute();
	}

	@Override
	public Boolean sadd(final String key, int expireTime, final Object... values) {
		return new RedisSingleCommand<Boolean>(JedisPool) {
			public Boolean commond(Jedis jedis) {
				String[] valueJsons = new String[values.length];
				for (int i = 0; i < values.length; i++) {
					Object value = values[i];
					String valueJson = null;
					if (!value.getClass().equals(String.class)) {
						valueJson = toJsonString(value);
					} else {
						valueJson = (String) value;
					}
					valueJsons[i] = valueJson;
				}
				jedis.sadd(key, valueJsons);
				return true;
			}
		}.execute();
	}

	@Override
	public <T> Set<T> smembers(String key, Class<T> clazz) {
		Set<String> set = this.smembers(key);
		if (set == null || set.size() == 0) {
			return null;
		}
		Set<T> result = new HashSet<T>();
		for (String value : set) {
			result.add(fromJson(clazz, value));
		}
		return result;
	}

	@Override
	public Set<String> smembers(final String key) {
		return new RedisSingleCommand<Set<String>>(JedisPool) {
			public Set<String> commond(Jedis jedis) {
				return jedis.smembers(key);
			}
		}.execute();
	}

	@Override
	public <T> T spop(String key, Class<T> clazz) {
		String value = this.spop(key);
		if (value == null) {
			return null;
		}
		return fromJson(clazz, value);
	}

	@Override
	public String spop(final String key) {
		return new RedisSingleCommand<String>(JedisPool) {
			public String commond(Jedis jedis) {
				return jedis.spop(key);
			}
		}.execute();
	}

	@Override
	public Boolean srem(final String key, final String... members) {
		return new RedisSingleCommand<Boolean>(JedisPool) {
			public Boolean commond(Jedis jedis) {
				jedis.srem(key, members);
				return true;
			}
		}.execute();
	}

	@Override
	public Boolean sismember(final String key, final String member) {
		return new RedisSingleCommand<Boolean>(JedisPool) {
			public Boolean commond(Jedis jedis) {
				return jedis.sismember(key, member);
			}
		}.execute();
	}

	@Override
	public Long scard(final String key) {
		return new RedisSingleCommand<Long>(JedisPool) {
			public Long commond(Jedis jedis) {
				return jedis.scard(key);
			}
		}.execute();
	}

	@Override
	public Boolean setnx(final String key, final Object value, final int expireTime) {
		return new RedisSingleCommand<Boolean>(JedisPool) {
			public Boolean commond(Jedis jedis) {
				String valueJson = null;
				if (!value.getClass().equals(String.class)) {
					valueJson = toJsonString(value);
				} else {
					valueJson = (String) value;
				}
				jedis.setnx(key, valueJson);
				if (expireTime > 0) {
					jedis.expire(key, expireTime);
				}
				return true;
			}
		}.execute();
	}

	@Override
	public Long decr(final String key) {
		return new RedisSingleCommand<Long>(JedisPool) {
			public Long commond(Jedis jedis) {
				return jedis.decr(key);
			}
		}.execute();
	}

	@Override
	public Long decrBy(final String key, final long n) {
		return new RedisSingleCommand<Long>(JedisPool) {
			public Long commond(Jedis jedis) {
				return jedis.decrBy(key, n);
			}
		}.execute();
	}

	@Override
	public Long incr(final String key) {
		return new RedisSingleCommand<Long>(JedisPool) {
			public Long commond(Jedis jedis) {
				return jedis.incr(key);
			}
		}.execute();
	}

	@Override
	public Long incrBy(final String key, final long n) {
		return new RedisSingleCommand<Long>(JedisPool) {
			public Long commond(Jedis jedis) {
				return jedis.incrBy(key, n);
			}
		}.execute();
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	@Override
	public long zadd(final String key, final Map<String, Double> scoreMembers) {
		return new RedisSingleCommand<Long>(JedisPool) {
			public Long commond(Jedis jedis) {
				return jedis.zadd(key, scoreMembers);
			}
		}.execute();
	}

	@Override
	public Set<String> zrangeByScore(final String key, final Double min, final Double max) {
		return new RedisSingleCommand<Set<String>>(JedisPool) {
			public Set<String> commond(Jedis jedis) {
				return jedis.zrangeByScore(key, min, max);
			}
		}.execute();
	}

	@Override
	public long zrem(final String key, final String... members) {
		return new RedisSingleCommand<Long>(JedisPool) {
			public Long commond(Jedis jedis) {
				return jedis.zrem(key, members);
			}
		}.execute();
	}

	@Override
	public Long hIncrBy(final String key, final String fieldname, final long n) {
		return new RedisSingleCommand<Long>(JedisPool) {
			public Long commond(Jedis jedis) {
				return jedis.hincrBy(key, fieldname, n);
			}
		}.execute();
	}

	@Override
	public long zadd(final String key, final double score, final String member) {
		return new RedisSingleCommand<Long>(JedisPool) {
			public Long commond(Jedis jedis) {
				return jedis.zadd(key, score, member);
			}
		}.execute();
	}

	@Override
	public String srandmember(final String key) {
		return new RedisSingleCommand<String>(JedisPool) {
			public String commond(Jedis jedis) {
				return jedis.srandmember(key);
			}
		}.execute();
	}

	@Override
	public List<String> srandmember(final String key, final int count) {
		return new RedisSingleCommand<List<String>>(JedisPool) {
			public List<String> commond(Jedis jedis) {
				return jedis.srandmember(key, count);
			}
		}.execute();
	}

	@Override
	public Long zremrangeByScore(final String key, final double start, final double end) {
		return new RedisSingleCommand<Long>(JedisPool) {
			public Long commond(Jedis jedis) {
				return jedis.zremrangeByScore(key, start, end);
			}
		}.execute();
	}

	@Override
	public Long zrank(final String key, final String member) {
		return new RedisSingleCommand<Long>(JedisPool) {
			public Long commond(Jedis jedis) {
				return jedis.zrank(key, member);
			}
		}.execute();
	}

	@Override
	public Double zscore(final String key, final String member) {
		return new RedisSingleCommand<Double>(JedisPool) {
			public Double commond(Jedis jedis) {
				return jedis.zscore(key, member);
			}
		}.execute();
	}

	@Override
	public Set<String> zrangeByScore(final String key, final double min, final double max, final int offset, final int count) {
		return new RedisSingleCommand<Set<String>>(JedisPool) {
			public Set<String> commond(Jedis jedis) {
				return jedis.zrangeByScore(key, min, max, offset, count);
			}
		}.execute();
	}

	@Override
	public Long zcard(final String key) {
		return new RedisSingleCommand<Long>(JedisPool) {
			public Long commond(Jedis jedis) {
				return jedis.zcard(key);
			}
		}.execute();
	}

	@Override
	public Boolean setBytes(final byte[] key, final byte[] value, final int expireTime) {
		return new RedisSingleCommand<Boolean>(JedisPool) {
			public Boolean commond(Jedis jedis) {
				jedis.set(key, value);
				if (expireTime > 0) {
					jedis.expire(key, expireTime);
				}
				return true;
			}
		}.execute();
	}

	@Override
	public byte[] getBytes(final byte[] key) {
		return new RedisSingleCommand<byte[]>(JedisPool) {
			public byte[] commond(Jedis jedis) {
				return jedis.get(key);
			}
		}.execute();
	}

}
