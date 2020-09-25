package cn.emay.redis.command.hash;

import cn.emay.json.JsonHelper;
import cn.emay.redis.command.RedisCommand;
import redis.clients.jedis.*;

import java.nio.charset.StandardCharsets;

/**
 * @author Frank
 */
public class HsetCommand implements RedisCommand<Boolean> {

    private final String key;

    private final String fieldname;

    private final Object value;

    private final int expireTime;

    private final String datePattern;

    public HsetCommand(String key, String fieldname, Object value, int expireTime, String datePattern) {
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
        Long execResult = 0L;
        Long expireResult = 0L;
        if (byte[].class.isAssignableFrom(value.getClass())) {
            if (bjcommand != null) {
                execResult = bjcommand.hset(key.getBytes(StandardCharsets.UTF_8), fieldname.getBytes(StandardCharsets.UTF_8), (byte[]) value);
            } else if (bjccommand != null) {
                execResult = bjccommand.hset(key.getBytes(StandardCharsets.UTF_8), fieldname.getBytes(StandardCharsets.UTF_8), (byte[]) value);
            }
        } else if (String.class.isAssignableFrom(value.getClass())) {
            execResult = command.hset(key, fieldname, (String) value);
        } else {
            execResult = command.hset(key, fieldname, JsonHelper.toJsonStringWithoutNull(value, datePattern));
        }
        if (expireTime > 0) {
            expireResult = command.expire(key, expireTime);
        }
        return execResult == 1 && expireResult == 1;
    }

}
