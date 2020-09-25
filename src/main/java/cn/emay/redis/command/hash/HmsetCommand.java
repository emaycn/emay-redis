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
public class HmsetCommand implements RedisCommand<Boolean> {

    private final String key;

    private final Map<String, ?> value;

    private final int expireTime;

    private final String datePattern;

    public HmsetCommand(String key, Map<String, ?> value, int expireTime, String datePattern) {
        this.key = key;
        this.value = value;
        this.expireTime = expireTime;
        this.datePattern = datePattern;
    }

    @Override
    public Boolean commond(Jedis client) {
        return this.exec(client, client, null);
    }

    @Override
    public Boolean commond(JedisCluster client) {
        return this.exec(client, null, client);
    }

    @Override
    public Boolean commond(ShardedJedis client) {
        return this.exec(client, client, null);
    }

    private Boolean exec(JedisCommands command, BinaryJedisCommands bjcommand, BinaryJedisClusterCommands bjccommand) {
        String result = "ERROR";
        Long expireResult = 0L;
        int size = value.size();
        Map<byte[], byte[]> bytemap = new HashMap<>(size);
        Map<String, String> stringmap = new HashMap<>(size);
        for (Entry<String, ?> entry : value.entrySet()) {
            if (byte[].class.isAssignableFrom(entry.getValue().getClass())) {
                bytemap.put(entry.getKey().getBytes(StandardCharsets.UTF_8), (byte[]) entry.getValue());
            } else if (String.class.isAssignableFrom(entry.getValue().getClass())) {
                stringmap.put(entry.getKey(), (String) entry.getValue());
            } else {
                stringmap.put(entry.getKey(), JsonHelper.toJsonStringWithoutNull(entry.getValue(), datePattern));
            }
        }
        if (!bytemap.isEmpty()) {
            if (bjcommand != null) {
                result = bjcommand.hmset(key.getBytes(StandardCharsets.UTF_8), bytemap);
            } else if (bjccommand != null) {
                result = bjccommand.hmset(key.getBytes(StandardCharsets.UTF_8), bytemap);
            }
        }
        if (!stringmap.isEmpty()) {
            result = command.hmset(key, stringmap);
        }
        if (expireTime > 0) {
            expireResult = command.expire(key, expireTime);
        }
        return "OK".equalsIgnoreCase(result) && expireResult == 1L;
    }

}
