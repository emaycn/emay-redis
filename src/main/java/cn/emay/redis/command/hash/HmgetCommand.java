package cn.emay.redis.command.hash;

import cn.emay.json.JsonHelper;
import cn.emay.redis.command.RedisCommand;
import redis.clients.jedis.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Frank
 */
public class HmgetCommand<K> implements RedisCommand<List<K>> {

    private final String key;

    private final Class<K> clazz;

    private final String[] fieldnames;

    private final String datePattern;

    public HmgetCommand(String key, Class<K> clazz, String datePattern, String... fieldnames) {
        this.key = key;
        this.clazz = clazz;
        this.fieldnames = fieldnames;
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
        if (byte[].class.isAssignableFrom(clazz)) {
            byte[][] bytes = new byte[fieldnames.length][];
            for (int i = 0; i < fieldnames.length; i++) {
                bytes[i] = fieldnames[i].getBytes(StandardCharsets.UTF_8);
            }
            List<byte[]> value = null;
            if (bjcommand != null) {
                value = bjcommand.hmget(key.getBytes(StandardCharsets.UTF_8), bytes);
            } else if (bjccommand != null) {
                value = bjccommand.hmget(key.getBytes(StandardCharsets.UTF_8), bytes);
            }
            return (List<K>) value;
        } else if (String.class.isAssignableFrom(clazz)) {
            return (List<K>) command.hmget(key, fieldnames);
        } else {
            List<String> stringlist1 = command.hmget(key, fieldnames);
            if (stringlist1 == null || stringlist1.isEmpty()) {
                return null;
            }
            List<K> stringlist = new ArrayList<>(stringlist1.size());
            for (String sr : stringlist1) {
                stringlist.add(JsonHelper.fromJson(clazz, sr, datePattern));
            }
            return stringlist;
        }
    }

}
