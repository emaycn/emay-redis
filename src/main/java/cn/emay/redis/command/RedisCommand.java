package cn.emay.redis.command;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ShardedJedis;

/**
 * 执行器
 *
 * @param <T>
 * @author Frank
 */
public interface RedisCommand<T> {

    /**
     * 单点redis执行方法
     *
     * @param client 客户端
     * @return 结果
     */
    T commond(Jedis client);

    /**
     * 集群redis执行方法
     *
     * @param client 客户端
     * @return 结果
     */
    T commond(JedisCluster client);

    /**
     * 分片redis执行方法
     *
     * @param client 客户端
     * @return 结果
     */
    T commond(ShardedJedis client);

}
