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
public class SremCommand implements RedisCommand<Long> {

    private final String key;

    private final Object[] members;

    private final String datePattern;

    public SremCommand(String key, String datePattern, Object... members) {
        this.key = key;
        this.members = members;
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
        for (Object obj : members) {
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
            length = command.srem(key, strvalues.toArray(new String[0]));
        }
        if (!bytvalues.isEmpty()) {
            if (bjcommand != null) {
                length = bjcommand.srem(key.getBytes(StandardCharsets.UTF_8), bytvalues.toArray(new byte[bytvalues.size()][]));
            } else if (bjccommand != null) {
                length = bjccommand.srem(key.getBytes(StandardCharsets.UTF_8), bytvalues.toArray(new byte[bytvalues.size()][]));
            }
        }
        return length;
    }

}
