package cn.emay.redis.command.hash;

import cn.emay.json.JsonHelper;
import cn.emay.redis.command.RedisCommand;
import redis.clients.jedis.*;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Frank
 */
public class HgetAllCommand<K> implements RedisCommand<Map<String, K>> {

    private final String key;

    private final Class<K> clazz;

    private final String datePattern;

    public HgetAllCommand(String key, Class<K> clazz, String datePattern) {
        this.key = key;
        this.clazz = clazz;
        this.datePattern = datePattern;
    }

    @Override
    public Map<String, K> commond(Jedis client) {
        return this.exec(client, client, null);
    }

    @Override
    public Map<String, K> commond(JedisCluster client) {
        return this.exec(client, null, client);
    }

    @Override
    public Map<String, K> commond(ShardedJedis client) {
        return this.exec(client, client, null);
    }

    @SuppressWarnings("unchecked")
    private Map<String, K> exec(JedisCommands command, BinaryJedisCommands bjcommand, BinaryJedisClusterCommands bjccommand) {
        if (byte[].class.isAssignableFrom(clazz)) {
            Map<byte[], byte[]> map = null;
            if (bjcommand != null) {
                map = bjcommand.hgetAll(key.getBytes(StandardCharsets.UTF_8));
            } else if (bjccommand != null) {
                map = bjccommand.hgetAll(key.getBytes(StandardCharsets.UTF_8));
            }
            if (map == null || map.isEmpty()) {
                return null;
            }
            Map<String, byte[]> map1 = new HashMap<>(map.size());
            for (Entry<byte[], byte[]> entry : map.entrySet()) {
                map1.put(new String(entry.getKey(), StandardCharsets.UTF_8), entry.getValue());
            }
            return (Map<String, K>) map1;
        } else if (String.class.isAssignableFrom(clazz)) {
            return (Map<String, K>) command.hgetAll(key);
        } else {
            Map<String, String> map = command.hgetAll(key);
            if (map == null || map.isEmpty()) {
                return null;
            }
            Map<String, K> map1 = new HashMap<>(map.size());
            for (Entry<String, String> entry : map.entrySet()) {
                map1.put(entry.getKey(), JsonHelper.fromJson(clazz, entry.getValue(), datePattern));
            }
            return map1;
        }
    }

}
