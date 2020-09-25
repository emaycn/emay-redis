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
public class LrangeCommand<K> implements RedisCommand<List<K>> {

    private final String key;

    private final Class<K> clazz;

    private final String datePattern;

    private final long start;

    private final long end;

    public LrangeCommand(String key, long start, long end, Class<K> clazz, String datePattern) {
        this.key = key;
        this.clazz = clazz;
        this.start = start;
        this.end = end;
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
        long startnew = (start <= 0) ? 0L : start;
        long endnew = (end < 0) ? -1L : end;
        List<K> lo = null;
        if (byte[].class.isAssignableFrom(clazz)) {
            if (bjcommand != null) {
                lo = (List<K>) bjcommand.lrange(key.getBytes(StandardCharsets.UTF_8), startnew, endnew);
            } else if (bjccommand != null) {
                lo = (List<K>) bjccommand.lrange(key.getBytes(StandardCharsets.UTF_8), startnew, endnew);
            }
        } else if (String.class.isAssignableFrom(clazz)) {
            lo = (List<K>) command.lrange(key, startnew, endnew);
        } else {
            List<String> list = command.lrange(key, startnew, endnew);
            if (list == null || list.isEmpty()) {
                return null;
            }
            lo = new ArrayList<>(list.size());
            for (String value : list) {
                K t = JsonHelper.fromJson(clazz, value, datePattern);
                if (t != null) {
                    lo.add(t);
                }
            }
        }
        return lo;
    }

}
