package cn.emay.redis.command.string;

import cn.emay.redis.command.RedisCommand;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;

/**
 * @author Frank
 */
public class DecrByCommand implements RedisCommand<Long> {

    private final String key;

    private final long number;

    public DecrByCommand(String key, long number) {
        this.key = key;
        this.number = number;
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
        return command.decrBy(key, number);
    }

}
