package cn.emay.redis.command.hash;

import cn.emay.json.JsonHelper;
import cn.emay.redis.command.RedisCommand;
import redis.clients.jedis.*;

import java.nio.charset.StandardCharsets;

/**
 * @author Frank
 */
public class HsetnxCommand implements RedisCommand<Boolean> {

    private final String key;

    private final String fieldname;

    private final Object value;

    private final int expireTime;

    private final String datePattern;

    public HsetnxCommand(String key, String fieldname, Object value, int expireTime, String datePattern) {
        this.key = key;
        this.value = value;
        this.fieldname = fieldname;
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
        long number = 0;
        if (byte[].class.isAssignableFrom(value.getClass())) {
            if (bjcommand != null) {
                number = bjcommand.hsetnx(key.getBytes(StandardCharsets.UTF_8), fieldname.getBytes(StandardCharsets.UTF_8), (byte[]) value);
            } else if (bjccommand != null) {
                number = bjccommand.hsetnx(key.getBytes(StandardCharsets.UTF_8), fieldname.getBytes(StandardCharsets.UTF_8), (byte[]) value);
            }
        } else if (String.class.isAssignableFrom(value.getClass())) {
            number = command.hsetnx(key, fieldname, (String) value);
        } else {
            number = command.hsetnx(key, fieldname, JsonHelper.toJsonStringWithoutNull(value, datePattern));
        }
        if (number != 0) {
            if (expireTime > 0) {
                command.expire(key, expireTime);
            }
            return true;
        } else {
            return false;
        }
    }

}
