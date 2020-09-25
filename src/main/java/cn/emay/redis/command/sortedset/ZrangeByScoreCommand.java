package cn.emay.redis.command.sortedset;

import cn.emay.redis.command.RedisCommand;
import redis.clients.jedis.*;

import java.util.Set;

/**
 * @author Frank
 */
public class ZrangeByScoreCommand implements RedisCommand<Set<Tuple>> {

    private final String key;

    private final double min;

    private final double max;

    public ZrangeByScoreCommand(String key, double min, double max) {
        this.key = key;
        this.min = min;
        this.max = max;
    }

    @Override
    public Set<Tuple> commond(Jedis client) {
        return this.exec(client);
    }

    @Override
    public Set<Tuple> commond(JedisCluster client) {
        return this.exec(client);
    }

    @Override
    public Set<Tuple> commond(ShardedJedis client) {
        return this.exec(client);
    }

    private Set<Tuple> exec(JedisCommands command) {
        return command.zrangeByScoreWithScores(key, min, max);
    }

}
