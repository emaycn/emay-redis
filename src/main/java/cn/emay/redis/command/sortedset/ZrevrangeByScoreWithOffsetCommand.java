package cn.emay.redis.command.sortedset;

import cn.emay.redis.command.RedisCommand;
import redis.clients.jedis.*;

import java.util.Set;

/**
 * @author Frank
 */
public class ZrevrangeByScoreWithOffsetCommand implements RedisCommand<Set<Tuple>> {

    private final String key;

    private final double min;

    private final double max;

    private final int offset;

    private final int count;

    public ZrevrangeByScoreWithOffsetCommand(String key, double min, double max, int offset, int count) {
        this.key = key;
        this.min = min;
        this.max = max;
        this.offset = offset;
        this.count = count;
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
        return command.zrevrangeByScoreWithScores(key, max, min, offset, count);
    }

}
