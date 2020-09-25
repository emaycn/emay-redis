package cn.emay.redis.impl;

import cn.emay.redis.RedisClient;
import cn.emay.redis.command.PipelineCommand;
import cn.emay.redis.command.RedisCommand;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

import java.util.List;
import java.util.Properties;

/**
 * Redis单点客户端
 *
 * @author Frank
 */
public class RedisSingleClient extends BaseRedisClient<JedisPool> implements RedisClient {

    /**
     * 客户端连接池
     */
    private JedisPool jedisPool;

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
     * @param host          redis地址
     * @param port          redis端口
     * @param timeoutMillis 超时时间
     * @param maxIdle       最大空闲线程数
     * @param maxTotal      最大线程数
     * @param minIdle       最小空闲线程数
     * @param maxWaitMillis 最大等待时间
     */
    public RedisSingleClient(String host, int port, int timeoutMillis, int maxIdle, int maxTotal, int minIdle, long maxWaitMillis) {
        this(host, port, timeoutMillis, maxIdle, maxTotal, minIdle, maxWaitMillis, null);
    }

    /**
     * 实例化并初始化
     *
     * @param host          redis地址
     * @param port          redis端口
     * @param timeoutMillis 超时时间
     * @param maxIdle       最大空闲线程数
     * @param maxTotal      最大线程数
     * @param minIdle       最小空闲线程数
     * @param maxWaitMillis 最大等待时间
     * @param datePattern   Json转换日期格式
     */
    public RedisSingleClient(String host, int port, int timeoutMillis, int maxIdle, int maxTotal, int minIdle, long maxWaitMillis, String datePattern) {
        this(host, port, timeoutMillis, maxIdle, maxTotal, minIdle, maxWaitMillis, datePattern, null);
    }

    /**
     * 实例化并初始化
     *
     * @param host          redis地址
     * @param port          redis端口
     * @param timeoutMillis 超时时间
     * @param maxIdle       最大空闲线程数
     * @param maxTotal      最大线程数
     * @param minIdle       最小空闲线程数
     * @param maxWaitMillis 最大等待时间
     * @param datePattern   Json转换日期格式
     * @param password      密码(非必填)
     */
    public RedisSingleClient(String host, int port, int timeoutMillis, int maxIdle, int maxTotal, int minIdle, long maxWaitMillis, String datePattern,
                             String password) {
        properties = new Properties();
        properties.setProperty("host", host);
        if (password != null) {
            properties.setProperty("password", password);
        }
        properties.setProperty("port", String.valueOf(port));
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
    public RedisSingleClient() {

    }

    @Override
    public void init() {
        if (jedisPool != null) {
            return;
        }
        datePattern = properties.getProperty("datePattern") == null ? "yyyy-MM-dd HH:mm:ss" : properties.getProperty("datePattern");
        String host = properties.getProperty("host").trim();
        int port = Integer.parseInt(properties.getProperty("port"));
        int timeout = Integer.parseInt(properties.getProperty("timeout"));
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
        if (password != null && !password.trim().equals("")) {
            jedisPool = new JedisPool(poolConfig, host, port, timeout, password);
        } else {
            jedisPool = new JedisPool(poolConfig, host, port, timeout);
        }
    }

    @Override
    public JedisPool getClient() {
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
        try (Jedis jedis = getClient().getResource()) {
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
        try (Jedis jedis = getClient().getResource(); Pipeline pipeline = jedis.pipelined()) {
            pipelineCommand.commond(pipeline);
            return pipelineCommand.getResult(pipeline);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
