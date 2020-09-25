package cn.emay.redis.command.list;

import cn.emay.json.JsonHelper;
import cn.emay.redis.command.RedisCommand;
import redis.clients.jedis.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Frank
 */
public class RpushCommand implements RedisCommand<Long> {

    private final String key;

    private final Object[] objects;

    private final int expireTime;

    private final String datePattern;

    public RpushCommand(String key, int expireTime, String datePattern, Object... objects) {
        this.key = key;
        this.objects = objects;
        this.expireTime = expireTime;
        this.datePattern = datePattern;
    }

    @Override
    public Long commond(Jedis client) {
        return this.exec(client, client, null);
    }

    @Override
    public Long commond(JedisCluster client) {
        return this.exec(client, null, client);
    }

    @Override
    public Long commond(ShardedJedis client) {
        return this.exec(client, client, null);
    }

    private Long exec(JedisCommands command, BinaryJedisCommands bjcommand, BinaryJedisClusterCommands bjccommand) {
        long length = 0;
        List<String> strvalues = new ArrayList<>();
        List<byte[]> bytvalues = new ArrayList<>();
        for (Object obj : objects) {
            if (obj == null) {
                continue;
            }
            if (byte[].class.isAssignableFrom(obj.getClass())) {
                bytvalues.add((byte[]) obj);
            } else if (String.class.isAssignableFrom(obj.getClass())) {
                strvalues.add((String) obj);
            } else {
                String value = JsonHelper.toJsonStringWithoutNull(obj, datePattern);
                if (value != null) {
                    strvalues.add(value);
                }
            }
        }
        if (!strvalues.isEmpty()) {
            length += command.rpush(key, strvalues.toArray(new String[0]));
        }
        if (!bytvalues.isEmpty()) {
            if (bjcommand != null) {
                length += bjcommand.rpush(key.getBytes(StandardCharsets.UTF_8), bytvalues.toArray(new byte[bytvalues.size()][]));
            } else if (bjccommand != null) {
                length += bjccommand.rpush(key.getBytes(StandardCharsets.UTF_8), bytvalues.toArray(new byte[bytvalues.size()][]));
            }
        }
        if (expireTime > 0) {
            command.expire(key, expireTime);
        }

        return length;
    }

}
