package cn.emay.redis.command.string;

import cn.emay.json.JsonHelper;
import cn.emay.redis.command.RedisCommand;
import redis.clients.jedis.*;

import java.nio.charset.StandardCharsets;

/**
 * @author Frank
 */
public class SetnxCommand implements RedisCommand<Boolean> {

    private final String key;

    private final Object value;

    private final int expireTime;

    private final String datePattern;

    public SetnxCommand(String key, Object value, int expireTime, String datePattern) {
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

    private Boolean exec(JedisCommands command, BinaryJedisCommands bjCommand, BinaryJedisClusterCommands bjcCommand) {
        long number = 0;
        if (byte[].class.isAssignableFrom(value.getClass())) {
            byte[] valuebytes = (byte[]) value;
            if (bjCommand != null) {
                number = bjCommand.setnx(key.getBytes(StandardCharsets.UTF_8), valuebytes);
            } else if (bjcCommand != null) {
                number = bjcCommand.setnx(key.getBytes(StandardCharsets.UTF_8), valuebytes);
            }
        } else if (String.class.isAssignableFrom(value.getClass())) {
            number = command.setnx(key, (String) value);
        } else {
            number = command.setnx(key, JsonHelper.toJsonStringWithoutNull(value, datePattern));
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
