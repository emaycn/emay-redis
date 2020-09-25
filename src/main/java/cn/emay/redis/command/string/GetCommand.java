package cn.emay.redis.command.string;

import cn.emay.json.JsonHelper;
import cn.emay.redis.command.RedisCommand;
import redis.clients.jedis.*;

import java.nio.charset.StandardCharsets;

/**
 * @author Frank
 */
public class GetCommand<K> implements RedisCommand<K> {

    private final String key;

    private final Class<K> clazz;

    private final String datePattern;

    public GetCommand(String key, Class<K> clazz, String datePattern) {
        this.key = key;
        this.clazz = clazz;
        this.datePattern = datePattern;
    }

    @Override
    public K commond(Jedis client) {
        return this.exec(client, client, null);
    }

    @Override
    public K commond(JedisCluster client) {
        return this.exec(client, null, client);
    }

    @Override
    public K commond(ShardedJedis client) {
        return this.exec(client, client, null);
    }

    @SuppressWarnings("unchecked")
    private K exec(JedisCommands command, BinaryJedisCommands bjcommand, BinaryJedisClusterCommands bjccommand) {
        if (byte[].class.isAssignableFrom(clazz)) {
            byte[] value = null;
            if (bjcommand != null) {
                value = bjcommand.get(key.getBytes(StandardCharsets.UTF_8));
            } else if (bjccommand != null) {
                value = bjccommand.get(key.getBytes(StandardCharsets.UTF_8));
            }
            return (K) value;
        } else if (String.class.isAssignableFrom(clazz)) {
            return (K) command.get(key);
        } else {
            String value = command.get(key);
            return value == null ? null : JsonHelper.fromJson(clazz, value, datePattern);
        }
    }

}
