package cn.emay.redis.command.sortedset;

import cn.emay.redis.command.RedisCommand;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;

/**
 * @author Frank
 */
public class ZremCommand implements RedisCommand<Long> {

    private final String key;

    private final String[] members;

    public ZremCommand(String key, String... members) {
        this.key = key;
        this.members = members;
    }

    @Override
    public Long commond(Jedis client) {
        return this.exec(client);
    }

    @Override
    public Long commond(JedisCluster client) {
        return this.exec(client);
    }

    @Override
    public Long commond(ShardedJedis client) {
        return this.exec(client);
    }

    private Long exec(JedisCommands command) {
        return command.zrem(key, members);
    }

}
