package cn.emay.store.redis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import cn.emay.json.JsonHelper;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class RedisClusterClient implements RedisClient {

	private JedisCluster jedisCluster;

	private Properties properties;

	public RedisClusterClient(String hosts, int timeout, int maxRedirections, int maxIdle, int maxTotal, int minIdle, long maxWaitMillis) {
		this(hosts, timeout, maxRedirections, maxIdle, maxTotal, minIdle, maxWaitMillis, null);
	}

	public RedisClusterClient(String hosts, int timeout, int maxRedirections, int maxIdle, int maxTotal, int minIdle, long maxWaitMillis, String datePattern) {
		properties = new Properties();
		properties.setProperty("hosts", hosts);
		properties.setProperty("maxRedirections", String.valueOf(maxRedirections));
		properties.setProperty("timeout", String.valueOf(timeout));
		properties.setProperty("maxIdle", String.valueOf(maxIdle));
		properties.setProperty("maxTotal", String.valueOf(maxTotal));
		properties.setProperty("minIdle", String.valueOf(minIdle));
		properties.setProperty("maxWaitMillis", String.valueOf(maxWaitMillis));
		properties.setProperty("datePattern", datePattern == null ? "yyyy-MM-dd HH:mm:ss" : datePattern);
		this.init();
	}

	public RedisClusterClient() {

	}

	public void init() {
		String datePattern = properties.getProperty("datePattern");
		properties.setProperty("datePattern", datePattern == null ? "yyyy-MM-dd HH:mm:ss" : datePattern);
		String hosts = properties.getProperty("hosts");
		int maxRedirections = Integer.valueOf(properties.getProperty("maxRedirections"));
		int timeout = Integer.valueOf(properties.getProperty("timeout"));
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		poolConfig.setMaxIdle(Integer.valueOf(properties.getProperty("maxIdle")));
		poolConfig.setMaxTotal(Integer.valueOf(properties.getProperty("maxTotal")));
		poolConfig.setMaxWaitMillis(Long.valueOf(properties.getProperty("maxWaitMillis")));
		poolConfig.setMinIdle(Integer.valueOf(properties.getProperty("minIdle")));
		Set<HostAndPort> set = new HashSet<HostAndPort>();
		String[] hostses = hosts.split(",");
		for (String host : hostses) {
			String[] ipAndPortArray = host.split(":");
			if (ipAndPortArray.length != 2) {
				throw new RuntimeException("host : " + host + " is error ! ");
			}
			int port;
			String ip = ipAndPortArray[0];
			try {
				port = Integer.valueOf(ipAndPortArray[1]);
			} catch (Exception e) {
				throw new RuntimeException(" port  is must number ! ");
			}
			set.add(new HostAndPort(ip, port));
		}
		jedisCluster = new JedisCluster(set, timeout, maxRedirections, poolConfig);

	}

	private String toJsonString(Object obj) {
		return JsonHelper.toJsonString(obj, properties.getProperty("datePattern"));
	}

	private <T> T fromJson(Class<T> clazz, String jsonString) {
		return JsonHelper.fromJson(clazz, jsonString, properties.getProperty("datePattern"));
	}

	public void close() {
		try {
			jedisCluster.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public JedisCluster getClient() {
		return jedisCluster;
	}

	@Override
	public Boolean exists(String key) {
		return this.getClient().exists(key);
	}

	@Override
	public Boolean del(String... keys) {
		this.getClient().del(keys);
		return true;
	}

	@Override
	public Boolean expire(String key, int seconds) {
		this.getClient().expire(key, seconds);
		return true;
	}

	@Override
	public Boolean set(String key, Object value, int expireTime) {
		String valueJson = null;
		if (!value.getClass().equals(String.class)) {
			valueJson = toJsonString(value);
		} else {
			valueJson = (String) value;
		}
		this.getClient().set(key, valueJson);
		if (expireTime > 0) {
			this.expire(key, expireTime);
		}
		return true;
	}

	@Override
	public String get(String key) {
		return this.getClient().get(key);
	}

	@Override
	public Object getObject(String key) {
		return this.getClient().get(key);
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
	public Boolean hdel(String key, String... fieldnames) {
		this.getClient().hdel(key, fieldnames);
		return true;
	}

	@Override
	public Boolean hmset(String key, Map<String, Object> value, int expireTime) {
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
		this.getClient().hmset(key, jsonMap);
		if (expireTime > 0) {
			this.getClient().expire(key, expireTime);
		}
		return true;
	}

	@Override
	public Boolean hset(String key, String fieldname, Object value, int expireTime) {
		String valueJson = null;
		if (!value.getClass().equals(String.class)) {
			valueJson = toJsonString(value);
		} else {
			valueJson = (String) value;
		}
		this.getClient().hset(key, fieldname, valueJson);
		if (expireTime > 0) {
			this.getClient().expire(key, expireTime);
		}
		return true;
	}

	@Override
	public Long hlen(String key) {
		return this.getClient().hlen(key);
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
	public String hget(String key, String fieldname) {
		return this.getClient().hget(key, fieldname);
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
	public List<String> hmget(String key, String... fieldnames) {
		return this.getClient().hmget(key, fieldnames);
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
	public Map<String, String> hgetAll(String key) {
		return this.getClient().hgetAll(key);
	}

	@Override
	public Boolean hexists(String key, String fieldname) {
		return this.getClient().hexists(key, fieldname);
	}

	@Override
	public Set<String> hkeys(String key) {
		return this.getClient().hkeys(key);
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

	private Boolean listpush(String key, int expireTime, boolean isLeft, Object... objects) {
		String[] values = new String[objects.length];
		for (int i = 0; i < objects.length; i++) {
			if (objects[i].getClass().equals(String.class)) {
				values[i] = (String) objects[i];
			} else {
				values[i] = toJsonString(objects[i]);
			}
		}
		if (isLeft) {
			this.getClient().lpush(key, values);
		} else {
			this.getClient().rpush(key, values);
		}
		if (expireTime > 0) {
			this.expire(key, expireTime);
		}
		return true;
	}

	private Long listlpush(String key, int expireTime, boolean isLeft, Object... objects) {
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
			result = this.getClient().lpush(key, values);
		} else {
			result = this.getClient().rpush(key, values);
		}
		if (expireTime > 0) {
			this.expire(key, expireTime);
		}
		return result;
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

	private String listPop(String key, boolean isBlocking, int blockingtimeout, boolean isLeft) {
		String value = null;
		if (isBlocking) {
			List<String> result = null;
			if (isLeft) {
				result = this.getClient().blpop(blockingtimeout, key);
			} else {
				result = this.getClient().brpop(blockingtimeout, key);
			}
			if (result != null && result.size() > 1) {
				value = result.get(1);
			}
		} else {
			if (isLeft) {
				value = this.getClient().lpop(key);
			} else {
				value = this.getClient().rpop(key);
			}
		}
		return value;
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
	public List<String> lrange(String key, Long start, Long end) {
		start = (start == null || start <= 0) ? 0l : start;
		end = (end == null || end < 0) ? -1l : end;
		return this.getClient().lrange(key, start, end);
	}

	@Override
	public Long llen(String key) {
		return this.getClient().llen(key);
	}

	@Override
	public Boolean sadd(String key, int expireTime, Object... values) {
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
		this.getClient().sadd(key, valueJsons);
		return true;
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
	public Set<String> smembers(String key) {
		return this.getClient().smembers(key);
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
	public String spop(String key) {
		return this.getClient().spop(key);
	}

	@Override
	public Boolean srem(String key, String... members) {
		this.getClient().srem(key, members);
		return true;
	}

	@Override
	public Boolean sismember(String key, String member) {
		return this.getClient().sismember(key, member);
	}

	@Override
	public Long scard(String key) {
		return this.getClient().scard(key);
	}

	@Override
	public Boolean setnx(String key, Object value, int expireTime) {
		String valueJson = null;
		if (!value.getClass().equals(String.class)) {
			valueJson = toJsonString(value);
		} else {
			valueJson = (String) value;
		}
		this.getClient().setnx(key, valueJson);
		if (expireTime > 0) {
			this.getClient().expire(key, expireTime);
		}
		return true;
	}

	@Override
	public Long decr(String key) {
		return this.getClient().decr(key);
	}

	@Override
	public Long decrBy(String key, long n) {
		return this.getClient().decrBy(key, n);
	}

	@Override
	public Long incr(String key) {
		return this.getClient().incr(key);
	}

	@Override
	public Long incrBy(String key, long n) {
		return this.getClient().incrBy(key, n);
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	@Override
	public long zadd(String key, Map<String, Double> scoreMembers) {
		if (scoreMembers == null || scoreMembers.size() == 0) {
			return 0;
		}
		return this.getClient().zadd(key, scoreMembers);
	}

	@Override
	public Set<String> zrangeByScore(String key, Double min, Double max) {
		return this.getClient().zrangeByScore(key, min, max);
	}

	@Override
	public long zrem(String key, String... members) {
		if (members == null || members.length == 0)
			return 0;
		return this.getClient().zrem(key, members);
	}

	@Override
	public Long hIncrBy(String key, String fieldname, long n) {
		return this.getClient().hincrBy(key, fieldname, n);
	}

	@Override
	public long zadd(String key, double score, String member) {
		return this.getClient().zadd(key, score, member);
	}

	@Override
	public String srandmember(String key) {
		return this.getClient().srandmember(key);
	}

	@Override
	public List<String> srandmember(String key, int count) {
		return this.getClient().srandmember(key, count);
	}

	@Override
	public Long zremrangeByScore(String key, double start, double end) {
		return this.getClient().zremrangeByScore(key, start, end);
	}

	@Override
	public Double zscore(String key, String member) {
		return this.getClient().zscore(key, member);
	}

	@Override
	public Long zrank(String key, String member) {
		return this.getClient().zrank(key, member);
	}

	@Override
	public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
		return this.getClient().zrangeByScore(key, min, max, offset, count);
	}

	@Override
	public Long zcard(String key) {
		return this.getClient().zcard(key);
	}

	@Override
	public Boolean setBytes(byte[] key, byte[] value, int expireTime) {
		this.getClient().set(key, value);
		if (expireTime > 0) {
			this.getClient().expire(key, expireTime);
		}
		return true;
	}

	@Override
	public byte[] getBytes(byte[] key) {
		return this.getClient().get(key);
	}
}
