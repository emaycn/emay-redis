package cn.emay.redis.command.common;

import cn.emay.redis.command.RedisCommand;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ShardedJedis;

/**
 * 检测剩余超时时间命令
 *
 * @author Frank
 */
public class TtlCommand implements RedisCommand<Long> {

    private final String key;

    public TtlCommand(String key) {
        this.key = key;
    }

    @Override
    public Long commond(Jedis client) {
        return client.ttl(key);
    }

    @Override
    public Long commond(JedisCluster client) {
        return client.ttl(key);
    }

    @Override
    public Long commond(ShardedJedis client) {
        return client.ttl(key);
    }

}
