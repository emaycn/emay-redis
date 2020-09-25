package cn.emay.redis.command.set;

import cn.emay.json.JsonHelper;
import cn.emay.redis.command.RedisCommand;
import redis.clients.jedis.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Frank
 */
public class SrandmemberCommand<K> implements RedisCommand<List<K>> {

    private final String key;

    private final Class<K> clazz;

    private final String datePattern;

    private final int count;

    public SrandmemberCommand(String key, Class<K> clazz, int count, String datePattern) {
        this.key = key;
        this.clazz = clazz;
        this.count = count;
        this.datePattern = datePattern;
    }

    @Override
    public List<K> commond(Jedis client) {
        return this.exec(client, client, null);
    }

    @Override
    public List<K> commond(JedisCluster client) {
        return this.exec(client, null, client);
    }

    @Override
    public List<K> commond(ShardedJedis client) {
        return this.exec(client, client, null);
    }

    @SuppressWarnings("unchecked")
    private List<K> exec(JedisCommands command, BinaryJedisCommands bjcommand, BinaryJedisClusterCommands bjccommand) {
        if (byte[].class.isAssignableFrom(clazz)) {
            List<byte[]> value = null;
            if (bjcommand != null) {
                value = bjcommand.srandmember(key.getBytes(StandardCharsets.UTF_8), count);
            } else if (bjccommand != null) {
                value = bjccommand.srandmember(key.getBytes(StandardCharsets.UTF_8), count);
            }
            return (List<K>) value;
        } else if (String.class.isAssignableFrom(clazz)) {
            return (List<K>) command.srandmember(key, count);
        } else {
            List<String> value = command.srandmember(key, count);
            if (value == null || value.isEmpty()) {
                return null;
            }
            List<K> list = new ArrayList<>();
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
