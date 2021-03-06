package cn.emay.redis.impl;

import cn.emay.redis.RedisClient;
import cn.emay.redis.command.PipelineCommand;
import cn.emay.redis.command.RedisCommand;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Redis单点客户端
 *
 * @author Frank
 */
public class RedisShardedClient extends BaseRedisClient<ShardedJedisPool> implements RedisClient {

    /**
     * 客户端连接池
     */
    private ShardedJedisPool jedisPool;

    /**
     * Json转换日期格式
     */
    private String datePattern;

    /**
     * 参数
     */
    private Properties properties;

    /**
     * 实例化并初始化
     *
     * @param hosts         集群节点地址，ip:port,ip:port 或ip:port;ip:port。 支持逗号以分号分隔符
     * @param timeoutMillis 超时时间
     * @param maxIdle       最大空闲线程数
     * @param maxTotal      最大线程数
     * @param minIdle       最小空闲线程数
     * @param maxWaitMillis 最大等待时间
     */
    public RedisShardedClient(String hosts, int timeoutMillis, int maxIdle, int maxTotal, int minIdle, long maxWaitMillis) {
        this(hosts, timeoutMillis, maxIdle, maxTotal, minIdle, maxWaitMillis, null);
    }

    /**
     * 实例化并初始化
     *
     * @param hosts         集群节点地址，ip:port,ip:port 或ip:port;ip:port。 支持逗号以分号分隔符
     * @param timeoutMillis 超时时间
     * @param maxIdle       最大空闲线程数
     * @param maxTotal      最大线程数
     * @param minIdle       最小空闲线程数
     * @param maxWaitMillis 最大等待时间
     * @param datePattern   Json转换日期格式
     */
    public RedisShardedClient(String hosts, int timeoutMillis, int maxIdle, int maxTotal, int minIdle, long maxWaitMillis, String datePattern) {
        this(hosts, timeoutMillis, maxIdle, maxTotal, minIdle, maxWaitMillis, datePattern, null);
    }

    /**
     * 实例化并初始化
     *
     * @param hosts         集群节点地址，ip:port,ip:port 或ip:port;ip:port。 支持逗号以分号分隔符
     * @param timeoutMillis 超时时间
     * @param maxIdle       最大空闲线程数
     * @param maxTotal      最大线程数
     * @param minIdle       最小空闲线程数
     * @param maxWaitMillis 最大等待时间
     * @param datePattern   Json转换日期格式
     * @param password      密码(非必填)
     */
    public RedisShardedClient(String hosts, int timeoutMillis, int maxIdle, int maxTotal, int minIdle, long maxWaitMillis, String datePattern,
                              String password) {
        properties = new Properties();
        properties.setProperty("hosts", hosts);
        if (password != null) {
            properties.setProperty("password", password);
        }
        properties.setProperty("timeout", String.valueOf(timeoutMillis));
        properties.setProperty("maxIdle", String.valueOf(maxIdle));
        properties.setProperty("maxTotal", String.valueOf(maxTotal));
        properties.setProperty("minIdle", String.valueOf(minIdle));
        properties.setProperty("maxWaitMillis", String.valueOf(maxWaitMillis));
        properties.setProperty("datePattern", datePattern == null ? "yyyy-MM-dd HH:mm:ss" : datePattern);
        this.init();
    }

    /**
     * 实例化并不初始化，需要setproperties()并调用init()主动初始化
     */
    public RedisShardedClient() {

    }

    @Override
    public void init() {
        if (jedisPool != null) {
            return;
        }
        datePattern = properties.getProperty("datePattern") == null ? "yyyy-MM-dd HH:mm:ss" : properties.getProperty("datePattern");
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(Integer.parseInt(properties.getProperty("maxIdle")));
        poolConfig.setMaxTotal(Integer.parseInt(properties.getProperty("maxTotal")));
        poolConfig.setMaxWaitMillis(Long.parseLong(properties.getProperty("maxWaitMillis")));
        poolConfig.setMinIdle(Integer.parseInt(properties.getProperty("minIdle")));

        poolConfig.setTestWhileIdle(true);
        poolConfig.setMinEvictableIdleTimeMillis(60000);
        poolConfig.setTimeBetweenEvictionRunsMillis(30000);
        poolConfig.setNumTestsPerEvictionRun(-1);

        String password = properties.getProperty("password");
        String hosts = properties.getProperty("hosts");
        int timeout = Integer.parseInt(properties.getProperty("timeout"));
        List<JedisShardInfo> infoList = new ArrayList<>();
        String[] hostses = hosts.split(",");
        for (String hostitem : hostses) {
            String[] hosten = hostitem.split(";");
            for (String host : hosten) {
                host = host.trim();
                if (host.length() == 0) {
                    continue;
                }
                String[] ipAndPortArray = host.split(":");
                if (ipAndPortArray.length != 2) {
                    throw new RuntimeException("host : " + host + " is error ! ");
                }
                int port;
                String ip = ipAndPortArray[0].trim();
                try {
                    port = Integer.parseInt(ipAndPortArray[1].trim());
                } catch (Exception e) {
                    throw new RuntimeException(" port  is must number ! ");
                }
                JedisShardInfo shardInfo = new JedisShardInfo(ip, port, timeout);
                if (password != null && !password.trim().equals("")) {
                    shardInfo.setPassword(password);
                }
                infoList.add(shardInfo);
            }
        }
        jedisPool = new ShardedJedisPool(poolConfig, infoList);
    }

    @Override
    public ShardedJedisPool getClient() {
        return jedisPool;
    }

    @Override
    public void close() {
        jedisPool.close();
    }

    public Properties getProperties() {
        return properties;
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    /* BASE */

    @Override
    public <T> T execCommand(RedisCommand<T> command) {
        try (ShardedJedis jedis = getClient().getResource()) {
            return command.commond(jedis);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getDatePattern() {
        return datePattern;
    }

    @Override
    public List<Object> execPipelineCommand(PipelineCommand pipelineCommand) {
        try (ShardedJedis jedis = getClient().getResource()) {
            ShardedJedisPipeline pipeline = jedis.pipelined();
            pipelineCommand.commond(pipeline);
            return pipelineCommand.getResult(pipeline);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
