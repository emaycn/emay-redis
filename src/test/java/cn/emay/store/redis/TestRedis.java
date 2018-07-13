package cn.emay.store.redis;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.emay.json.JsonHelper;
import cn.emay.store.redis.command.RedisCommand;
import cn.emay.store.redis.command.common.TtlCommand;
import cn.emay.store.redis.impl.RedisClusterClient;
import cn.emay.store.redis.impl.RedisShardedClient;
import cn.emay.store.redis.impl.RedisSingleClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ShardedJedis;

/**
 * Redis 测试
 * 
 * @author Frank
 *
 */
public class TestRedis {

	static RedisClient redis;

	public static void main(String[] args) throws InterruptedException {
		redis = RedisSingleClient();
		// redis = RedisClusterClient();
		// redis = RedisShardedClient();
		testBase();
		testCommon();
		testHash();
	}
	
	protected static void testString() {
		
	}

	protected static void testHash() {
		String key = "testHash";

		{
			Map<String, Object> params1 = new HashMap<>();
			params1.put("11", "str1");
			params1.put("12", "str2");
			params1.put("13", "str3");
			redis.hmset(key, params1, 10);
			printIsRight("hmset " + key + " " + params1, true);
			List<String> list1 = redis.hmget(key, String.class, "11", "12", "13");
			boolean ok = true;
			for (String s : list1) {
				boolean is = false;
				for (Object o : params1.values()) {
					if (o.equals(s)) {
						is = true;
						break;
					}
				}
				ok &= is;
			}
			printIsRight("hmget " + key + " 11,12,13", ok & list1.size() == 3);
			Map<String, String> map1 = redis.hgetAll(key, String.class);
			ok = true;
			for (String key1 : map1.keySet()) {
				Object obj = params1.get(key1);
				ok &= obj.equals(map1.get(key1));
			}
			printIsRight("hgetAll " + key, ok & map1.size() == 3);
			redis.del(key);

			Map<String, Object> params2 = new HashMap<>();
			params2.put("21", new byte[] { 0, 0, 1 });
			params2.put("22", new byte[] { 0, 1, 1 });
			params2.put("23", new byte[] { 1, 1, 1 });
			redis.hmset(key, params2, 10);
			printIsRight("hmset " + key + " " + params2, true);
			List<byte[]> list2 = redis.hmget(key, byte[].class, "21", "22", "23");
			ok = true;
			for (byte[] s : list2) {
				boolean is = false;
				for (Object o : params2.values()) {
					if (Arrays.equals(s, (byte[]) o)) {
						is = true;
						break;
					}
				}
				ok &= is;
			}
			printIsRight("hmget " + key + " 21,22,23", ok & list2.size() == 3);
			Map<String, byte[]> map2 = redis.hgetAll(key, byte[].class);
			ok = true;
			for (String key1 : map2.keySet()) {
				Object obj = params2.get(key1);
				ok &= Arrays.equals(map2.get(key1), (byte[]) obj);
			}
			printIsRight("hgetAll " + key, ok & map2.size() == 3);
			redis.del(key);

			Map<String, Object> params3 = new HashMap<>();
			params3.put("31", new TtlCommand("c1"));
			params3.put("32", new TtlCommand("c2"));
			params3.put("33", new TtlCommand("c3"));
			redis.hmset(key, params3, 10);
			printIsRight("hmset " + key + " " + params3, true);
			List<TtlCommand> list3 = redis.hmget(key, TtlCommand.class, "31", "32", "33");
			ok = true;
			for (TtlCommand s : list3) {
				boolean is = false;
				for (Object o : params3.values()) {
					if (JsonHelper.toJsonString(s).equals(JsonHelper.toJsonString(o))) {
						is = true;
						break;
					}
				}
				ok &= is;
			}
			printIsRight("hmget " + key + " 31,32,33", ok & list3.size() == 3);
			Map<String, TtlCommand> map3 = redis.hgetAll(key, TtlCommand.class);
			ok = true;
			for (String key1 : map3.keySet()) {
				Object obj = params3.get(key1);
				ok &= JsonHelper.toJsonString(obj).equals(JsonHelper.toJsonString(map3.get(key1)));
			}
			printIsRight("hgetAll " + key, ok & map3.size() == 3);
			redis.del(key);

		}

		{
			String field1 = "fieldtest1";
			byte[] value1 = { 0, 1, 1, 0 };
			String field2 = "fieldtest2";
			String value2 = "thisis my 110";
			String field3 = "fieldtest3";
			TtlCommand value3 = new TtlCommand(key);

			redis.hset(key, field1, value1, 10);
			printIsRight("hset " + key + " " + field1 + " 0,1,1,0 ", true);
			redis.hset(key, field2, value2, 10);
			printIsRight("hset " + key + " " + field2 + " " + value2, true);
			redis.hset(key, field3, value3, 10);
			printIsRight("hset " + key + " " + field3 + " " + JsonHelper.toJsonString(value3), true);

			Set<String> set = redis.hkeys(key);
			boolean isok = set.size() == 3;
			isok &= set.contains(field3);
			isok &= set.contains(field2);
			isok &= set.contains(field1);
			printIsRight("hkeys " + set, isok);
			long length = redis.hlen(key);
			printIsRight("hlen " + length, length == 3);

			boolean isHas = redis.hexists(key, field1);
			printIsRight("hexists " + key + " " + field1, isHas);
			isHas = redis.hexists(key, field2);
			printIsRight("hexists " + key + " " + field2, isHas);
			isHas = redis.hexists(key, field3);
			printIsRight("hexists " + key + " " + field3, isHas);

			byte[] v1 = redis.hget(key, field1, byte[].class);
			printIsRight("hget " + key + " " + field1, Arrays.equals(value1, v1));
			String v2 = redis.hget(key, field2, String.class);
			printIsRight("hget " + key + " " + field2, v2.equals(value2));
			TtlCommand v3 = redis.hget(key, field3, TtlCommand.class);
			printIsRight("hget " + key + " " + field3, JsonHelper.toJsonString(value3).equals(JsonHelper.toJsonString(v3)));

			isHas = redis.hsetnx(key, field1, new byte[] { 1, 1, 1, 1 }, 10);
			printIsRight("hsetnx " + key + " " + field1 + " 1,1,1,1", !isHas);
			v1 = redis.hget(key, field1, byte[].class);
			printIsRight("hget " + key + " " + field1, Arrays.equals(value1, v1));

			isHas = redis.hsetnx(key, field2, "hello", 10);
			printIsRight("hsetnx " + key + " " + field2 + " hello", !isHas);
			v2 = redis.hget(key, field2, String.class);
			printIsRight("hget " + key + " " + field2, v2.equals(value2));

			isHas = redis.hsetnx(key, field3, new TtlCommand("hello"), 10);
			printIsRight("hsetnx " + key + " " + field3 + " " + JsonHelper.toJsonString(new TtlCommand("hello")), !isHas);
			v3 = redis.hget(key, field3, TtlCommand.class);
			printIsRight("hget " + key + " " + field3, JsonHelper.toJsonString(value3).equals(JsonHelper.toJsonString(v3)));

			redis.hdel(key, field1);
			printIsRight("hdel " + key + " " + field1, true);
			redis.hdel(key, field2);
			printIsRight("hdel " + key + " " + field2, true);
			redis.hdel(key, field3);
			printIsRight("hdel " + key + " " + field3, true);

			length = redis.hlen(key);
			printIsRight("hlen " + length, length == 0);

			redis.del(key);
		}

		{
			String field1 = "fieldtest1";
			byte[] value1 = { 0, 1, 1, 0 };
			String field2 = "fieldtest2";
			String value2 = "thisis my 110";
			String field3 = "fieldtest3";
			TtlCommand value3 = new TtlCommand(key);

			boolean isok = redis.hsetnx(key, field1, value1, 10);
			printIsRight("hsetnx " + key + " " + field1 + " 0,1,1,0 ", isok);
			isok = redis.hsetnx(key, field2, value2, 10);
			printIsRight("hsetnx " + key + " " + field2 + " " + value2, isok);
			isok = redis.hsetnx(key, field3, value3, 10);
			printIsRight("hsetnx " + key + " " + field3 + " " + JsonHelper.toJsonString(value3), isok);

			byte[] v1 = redis.hget(key, field1, byte[].class);
			printIsRight("hget " + key + " " + field1, Arrays.equals(value1, v1));
			String v2 = redis.hget(key, field2, String.class);
			printIsRight("hget " + key + " " + field2, v2.equals(value2));
			TtlCommand v3 = redis.hget(key, field3, TtlCommand.class);
			printIsRight("hget " + key + " " + field3, JsonHelper.toJsonString(value3).equals(JsonHelper.toJsonString(v3)));

			redis.del(key);
		}

		{
			String field1 = "fieldtest1";
			int value1 = 1;

			redis.hset(key, field1, value1, 10);
			printIsRight("hset " + key + " " + field1 + " " + value1, true);
			long val = redis.hIncrBy(key, field1, 2);
			printIsRight("hIncrBy " + key + " " + field1 + " " + 2, val == 3);
			val = redis.hIncrBy(key, field1, -3);
			printIsRight("hIncrBy " + key + " " + field1 + " " + "-3", val == 0);

			redis.del(key);
		}

		printIsRight("**Hash测试**", true);
	}

	protected static void testCommon() throws InterruptedException {
		String key = "testCommon";
		TtlCommand ttlc = new TtlCommand(key);

		redis.set(key, ttlc, 2);
		printIsRight("set " + key + "=" + JsonHelper.toJsonString(ttlc) + " expire 2s", true);
		boolean exists1 = redis.exists(key);
		printIsRight("exists " + exists1, exists1);
		redis.del(key);
		printIsRight("del " + key, true);
		boolean exists2 = redis.exists(key);
		printIsRight("exists " + exists2, !exists2);
		redis.set(key, ttlc, 2);
		printIsRight("set " + key + "=" + JsonHelper.toJsonString(ttlc) + " expire 2s", true);
		printIsRight("sleep 1s", true);
		Thread.sleep(1000l);
		long ttl0 = redis.ttl(key);
		printIsRight("ttl " + ttl0, ttl0 >= 1);
		boolean exists3 = redis.exists(key);
		printIsRight("exists " + exists3, exists3);
		redis.persist(key);
		printIsRight("persist key expire", true);
		printIsRight("sleep 1s", true);
		Thread.sleep(1100l);
		long ttl1 = redis.ttl(key);
		printIsRight("ttl " + ttl1, ttl1 == -1);
		boolean exists4 = redis.exists(key);
		printIsRight("exists " + exists4, exists4);
		TtlCommand ttlc1 = redis.get(key, TtlCommand.class);
		printIsRight("get " + key + "=" + JsonHelper.toJsonString(ttlc1), JsonHelper.toJsonString(ttlc).equals(JsonHelper.toJsonString(ttlc1)));
		redis.expire(key, 1);
		printIsRight("expire 1s", true);
		long ttl2 = redis.ttl(key);
		printIsRight("ttl " + ttl2, ttl2 >= 0);
		printIsRight("sleep 1s", true);
		Thread.sleep(1100l);
		boolean exists5 = redis.exists(key);
		printIsRight("exists " + exists5, !exists5);

		printIsRight("**Common测试**", true);
		redis.del(key);
	}

	protected static void testBase() {
		String key = "testbase";
		String value = redis.getDatePattern();
		redis.execCommand(new RedisCommand<String>() {

			@Override
			public String commond(Jedis client) {
				client.set(key, value);
				client.expire(key, 5);
				return null;
			}

			@Override
			public String commond(JedisCluster client) {
				client.set(key, value);
				client.expire(key, 5);
				return null;
			}

			@Override
			public String commond(ShardedJedis client) {
				client.set(key, value);
				client.expire(key, 5);
				return null;
			}
		});
		printIsRight("set " + key + "=" + value, true);
		String value1 = redis.execCommand(new RedisCommand<String>() {

			@Override
			public String commond(Jedis client) {
				return client.get(key);
			}

			@Override
			public String commond(JedisCluster client) {
				return client.get(key);
			}

			@Override
			public String commond(ShardedJedis client) {
				return client.get(key);
			}

		});
		printIsRight("get " + key + "=" + value1, true);
		printIsRight("**基础测试**", (value1.equals(value)));
		redis.del(key);
	}

	protected static void printIsRight(String log, boolean isRight) {
		if (!isRight) {
			System.err.println("测试失败:" + log);
			System.exit(-1);
		} else {
			System.out.println("测试通过:" + log);
		}
	}

	protected static RedisClusterClient RedisClusterClient() {
		return new RedisClusterClient("127.0.0.1:6379,127.0.0.1:6378,127.0.0.1:6377,127.0.0.1:6376,127.0.0.1:6375,127.0.0.1:6374", 2000, 6, 4, 8, 1, 2000);
	}

	protected static RedisShardedClient RedisShardedClient() {
		return new RedisShardedClient("127.0.0.1:6379", 2000, 4, 8, 1, 2000);
	}

	protected static RedisSingleClient RedisSingleClient() {
		return new RedisSingleClient("127.0.0.1", 6379, 2000, 4, 8, 1, 2000);
	}

}
