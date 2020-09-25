package cn.emay.redis.command.set;

import cn.emay.json.JsonHelper;
import cn.emay.redis.command.RedisCommand;
import redis.clients.jedis.*;

import java.nio.charset.StandardCharsets;

/**
 * @author Frank
 */
public class SismemberCommand implements RedisCommand<Boolean> {

    private final String key;

    private final Object member;

    private final String datePattern;

    public SismemberCommand(String key, Object member, String datePattern) {
        this.key = key;
        this.member = member;
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
        boolean isHas = false;
        if (byte[].class.isAssignableFrom(member.getClass())) {
            if (bjcommand != null) {
                isHas = bjcommand.sismember(key.getBytes(StandardCharsets.UTF_8), (byte[]) member);
            } else if (bjccommand != null) {
                isHas = bjccommand.sismember(key.getBytes(StandardCharsets.UTF_8), (byte[]) member);
            }
        } else if (String.class.isAssignableFrom(member.getClass())) {
            isHas = command.sismember(key, (String) member);
        } else {
            String value = JsonHelper.toJsonStringWithoutNull(member, datePattern);
            if (value != null) {
                isHas = command.sismember(key, value);
            }
        }
        return isHas;
    }

}
