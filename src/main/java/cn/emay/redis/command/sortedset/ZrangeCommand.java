package cn.emay.redis.command.sortedset;

import cn.emay.redis.command.RedisCommand;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;

import java.util.Set;

/**
 * @author Frank
 */
public class ZrangeCommand implements RedisCommand<Set<String>> {

    private final String key;

    private final long start;

    private final long end;

    public ZrangeCommand(String key, long start, long end) {
        this.key = key;
        this.start = start;
        this.end = end;
    }

    @Override
    public Set<String> commond(Jedis client) {
        return this.exec(client);
    }

    @Override
    public Set<String> commond(JedisCluster client) {
        return this.exec(client);
    }

    @Override
    public Set<String> commond(ShardedJedis client) {
        return this.exec(client);
    }

    private Set<String> exec(JedisCommands command) {
        return command.zrange(key, start, end);
    }

}
