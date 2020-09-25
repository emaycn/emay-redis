package cn.emay.redis.command.hash;

import cn.emay.redis.command.RedisCommand;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ShardedJedis;

/**
 * @author Frank
 */
public class HdelCommand implements RedisCommand<Long> {

    private final String key;

    private final String[] fieldname;

    public HdelCommand(String key, String... fieldname) {
        this.key = key;
        this.fieldname = fieldname;
    }

    @Override
    public Long commond(Jedis client) {
        return client.hdel(key, fieldname);
    }

    @Override
    public Long commond(JedisCluster client) {
        return client.hdel(key, fieldname);
    }

    @Override
    public Long commond(ShardedJedis client) {
        return client.hdel(key, fieldname);
    }

}
