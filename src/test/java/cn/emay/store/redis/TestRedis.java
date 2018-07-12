package cn.emay.store.redis;

import cn.emay.store.redis.impl.RedisClusterClient;
import cn.emay.store.redis.impl.RedisShardedClient;
import cn.emay.store.redis.impl.RedisSingleClient;

public class TestRedis {

	public static void main(String[] args) {
		RedisClient client;
		client = RedisSingleClient();
		client = RedisClusterClient();
		client = RedisShardedClient();
		client.get("1");

	}

	private static RedisClusterClient RedisClusterClient() {
		return new RedisClusterClient("127.0.0.1:6379,127.0.0.1:6378,127.0.0.1:6377,127.0.0.1:6376,127.0.0.1:6375,127.0.0.1:6374", 2000, 6, 4, 8, 1, 2000);
	}

	private static RedisShardedClient RedisShardedClient() {
		return new RedisShardedClient("127.0.0.1:6379", 2000, 4, 8, 1, 2000);
	}

	private static RedisSingleClient RedisSingleClient() {
		return new RedisSingleClient("127.0.0.1", 6379, 2000, 4, 8, 1, 2000);
	}

}
