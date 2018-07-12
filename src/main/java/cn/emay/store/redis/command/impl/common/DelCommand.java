package cn.emay.store.redis.command.impl.common;

import cn.emay.store.redis.command.RedisCommand;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.MultiKeyCommands;
import redis.clients.jedis.MultiKeyJedisClusterCommands;
import redis.clients.jedis.ShardedJedis;

public class DelCommand implements RedisCommand<Void> {

	private String[] key;

	public DelCommand(String... key) {
		this.key = key;
	}

	@Override
	public Void commond(Jedis client) {
		this.exec(client, null, null);
		return null;
	}

	@Override
	public Void commond(JedisCluster client) {
		this.exec(null, client, null);
		return null;
	}

	@Override
	public Void commond(ShardedJedis client) {
		this.exec(null, null, client);
		return null;
	}

	private void exec(MultiKeyCommands jedis, MultiKeyJedisClusterCommands cluster, ShardedJedis shard) {
		if (jedis != null) {
			jedis.del(key);
		}
		if (cluster != null) {
			cluster.del(key);
		}
		if (shard != null) {
			for (String k : key) {
				shard.del(k);
			}
		}
	}

}
