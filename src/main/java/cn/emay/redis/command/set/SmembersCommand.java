package cn.emay.redis.command.set;

import cn.emay.json.JsonHelper;
import cn.emay.redis.command.RedisCommand;
import redis.clients.jedis.*;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Frank
 */
public class SmembersCommand<K> implements RedisCommand<Set<K>> {

    private final String key;

    private final Class<K> clazz;

    private final String datePattern;

    public SmembersCommand(String key, Class<K> clazz, String datePattern) {
        this.key = key;
        this.clazz = clazz;
        this.datePattern = datePattern;
    }

    @Override
    public Set<K> commond(Jedis client) {
        return this.exec(client, client, null);
    }

    @Override
    public Set<K> commond(JedisCluster client) {
        return this.exec(client, null, client);
    }

    @Override
    public Set<K> commond(ShardedJedis client) {
        return this.exec(client, client, null);
    }

    @SuppressWarnings("unchecked")
    private Set<K> exec(JedisCommands command, BinaryJedisCommands bjcommand, BinaryJedisClusterCommands bjccommand) {
        if (byte[].class.isAssignableFrom(clazz)) {
            Set<byte[]> value = null;
            if (bjcommand != null) {
                value = bjcommand.smembers(key.getBytes(StandardCharsets.UTF_8));
            } else if (bjccommand != null) {
                value = bjccommand.smembers(key.getBytes(StandardCharsets.UTF_8));
            }
            return (Set<K>) value;
        } else if (String.class.isAssignableFrom(clazz)) {
            return (Set<K>) command.smembers(key);
        } else {
            Set<String> value = command.smembers(key);
            if (value == null || value.isEmpty()) {
                return null;
            }
            Set<K> list = new HashSet<>();
            for (String va : value) {
                K json = JsonHelper.fromJson(clazz, va, datePattern);
                if (json != null) {
                    list.add(json);
                }
            }
            return list;
        }
    }

}
