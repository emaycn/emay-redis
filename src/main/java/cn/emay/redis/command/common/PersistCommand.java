package cn.emay.redis.command.common;

import cn.emay.redis.command.RedisCommand;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ShardedJedis;

/**
 * 移除超时时间命令
 *
 * @author Frank
 */
public class PersistCommand implements RedisCommand<Boolean> {

    private final String key;

    public PersistCommand(String key) {
        this.key = key;
    }

    @Override
    public Boolean commond(Jedis client) {
        return client.persist(key) == 1;
    }

    @Override
    public Boolean commond(JedisCluster client) {
        return client.persist(key) == 1;
    }

    @Override
    public Boolean commond(ShardedJedis client) {
        return client.persist(key) == 1;
    }

}
