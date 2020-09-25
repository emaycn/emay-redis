package cn.emay.redis.command.string;

import cn.emay.json.JsonHelper;
import cn.emay.redis.command.RedisCommand;
import redis.clients.jedis.*;

import java.nio.charset.StandardCharsets;

/**
 * @author Frank
 */
public class SetCommand implements RedisCommand<Boolean> {

    private final String key;

    private final Object value;

    private final int expireTime;

    private final String datePattern;

    public SetCommand(String key, Object value, int expireTime, String datePattern) {
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
        String setResult = null;
        if (byte[].class.isAssignableFrom(value.getClass())) {
            byte[] valuebytes = (byte[]) value;
            if (expireTime > 0) {
                if (bjcommand != null) {
                    setResult = bjcommand.setex(key.getBytes(StandardCharsets.UTF_8), expireTime, valuebytes);
                } else if (bjccommand != null) {
                    setResult = bjccommand.setex(key.getBytes(StandardCharsets.UTF_8), expireTime, valuebytes);
                }
            } else {
                if (bjcommand != null) {
                    setResult = bjcommand.set(key.getBytes(StandardCharsets.UTF_8), valuebytes);
                } else if (bjccommand != null) {
                    setResult = bjccommand.set(key.getBytes(StandardCharsets.UTF_8), valuebytes);
                }
            }
        } else if (String.class.isAssignableFrom(value.getClass())) {
            if (expireTime > 0) {
                setResult = command.setex(key, expireTime, (String) value);
            } else {
                setResult = command.set(key, (String) value);
            }
        } else {
            if (expireTime > 0) {
                setResult = command.setex(key, expireTime, JsonHelper.toJsonStringWithoutNull(value, datePattern));
            } else {
                setResult = command.set(key, JsonHelper.toJsonStringWithoutNull(value, datePattern));
            }
        }
        return "OK".equalsIgnoreCase(setResult);
    }

}
