package cn.emay.redis.command.sortedset;

import cn.emay.redis.command.RedisCommand;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;

public class ZincrbyCommand implements RedisCommand<Double> {

    private final String key;

    private final String member;

    private final double increment;

    public ZincrbyCommand(String key, double increment, String member) {
        this.key = key;
        this.member = member;
        this.increment = increment;
    }

    @Override
    public Double commond(Jedis client) {
        return this.exec(client);
    }

    @Override
    public Double commond(JedisCluster client) {
        return this.exec(client);
    }

    @Override
    public Double commond(ShardedJedis client) {
        return this.exec(client);
    }

    private Double exec(JedisCommands command) {
        return command.zincrby(key, increment, member);
    }

}
