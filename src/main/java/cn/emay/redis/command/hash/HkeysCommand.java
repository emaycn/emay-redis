package cn.emay.redis.command.hash;

import cn.emay.redis.command.RedisCommand;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;

import java.util.Set;

/**
 * @author Frank
 */
public class HkeysCommand implements RedisCommand<Set<String>> {

    private final String key;

    public HkeysCommand(String key) {
        this.key = key;
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
        return command.hkeys(key);
    }

}
