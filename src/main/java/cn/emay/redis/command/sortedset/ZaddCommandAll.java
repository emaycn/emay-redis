package cn.emay.redis.command.sortedset;

import cn.emay.redis.command.RedisCommand;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;

import java.util.Map;

/**
 * @author Frank
 */
public class ZaddCommandAll implements RedisCommand<Long> {

    private final String key;

    private final Map<String, Double> scoreMembers;

    public ZaddCommandAll(String key, Map<String, Double> scoreMembers) {
        this.key = key;
        this.scoreMembers = scoreMembers;
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
        return command.zadd(key, scoreMembers);
    }

}
