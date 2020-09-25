package cn.emay.redis.command.common;

import cn.emay.redis.command.RedisCommand;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ShardedJedis;

/**
 * 设置超时时间命令
 *
 * @author Frank
 */
public class ExpireCommand implements RedisCommand<Boolean> {

    private final String key;

    private final int seconds;

    public ExpireCommand(String key, int seconds) {
        this.key = key;
        this.seconds = seconds;
    }

    @Override
    public Boolean commond(Jedis client) {
        return client.expire(key, seconds) == 1;
    }

    @Override
    public Boolean commond(JedisCluster client) {
        return client.expire(key, seconds) == 1;
    }

    @Override
    public Boolean commond(ShardedJedis client) {
        return client.expire(key, seconds) == 1;
    }

}
