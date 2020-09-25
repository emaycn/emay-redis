package cn.emay.redis.impl;

import cn.emay.redis.RedisClient;
import cn.emay.redis.command.PipelineCommand;
import cn.emay.redis.command.RedisCommand;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Redis集群客户端
 *
 * @author Frank
 */
public class RedisClusterClient extends BaseRedisClient<JedisCluster> implements RedisClient {

    /**
     * 集群客户端
     */
    private JedisCluster jedisCluster;

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
     * @param hosts           集群节点地址，ip:port,ip:port 或ip:port;ip:port。 支持逗号以分号分隔符
     * @param timeoutMillis   超时时间
     * @param maxRedirections 最大寻址次数
     * @param maxIdle         最大空闲线程数
     * @param maxTotal        最大线程数
     * @param minIdle         最小空闲线程数
     * @param maxWaitMillis   最大等待时间
     */
    public RedisClusterClient(String hosts, int timeoutMillis, int maxRedirections, int maxIdle, int maxTotal, int minIdle, long maxWaitMillis) {
        this(hosts, timeoutMillis, maxRedirections, maxIdle, maxTotal, minIdle, maxWaitMillis, null);
    }

    /**
     * 实例化并初始化
     *
     * @param hosts           集群节点地址，ip:port,ip:port 或ip:port;ip:port。 支持逗号以分号分隔符
     * @param timeoutMillis   超时时间
     * @param maxRedirections 最大寻址次数
     * @param maxIdle         最大空闲线程数
     * @param maxTotal        最大线程数
     * @param minIdle         最小空闲线程数
     * @param maxWaitMillis   最大等待时间
     * @param datePattern     Json转换日期格式
     */
    public RedisClusterClient(String hosts, int timeoutMillis, int maxRedirections, int maxIdle, int maxTotal, int minIdle, long maxWaitMillis,
                              String datePattern) {
        this(hosts, timeoutMillis, maxRedirections, maxIdle, maxTotal, minIdle, maxWaitMillis, datePattern, null);
    }

    /**
     * 实例化并初始化
     *
     * @param hosts           集群节点地址，ip:port,ip:port 或ip:port;ip:port。 支持逗号以分号分隔符
     * @param timeoutMillis   超时时间
     * @param maxRedirections 最大寻址次数
     * @param maxIdle         最大空闲线程数
     * @param maxTotal        最大线程数
     * @param minIdle         最小空闲线程数
     * @param maxWaitMillis   最大等待时间
     * @param datePattern     Json转换日期格式
     * @param password        密码(非必填)
     */
    public RedisClusterClient(String hosts, int timeoutMillis, int maxRedirections, int maxIdle, int maxTotal, int minIdle, long maxWaitMillis,
                              String datePattern, String password) {
        properties = new Properties();
        properties.setProperty("hosts", hosts);
        if (password != null) {
            properties.setProperty("password", password);
        }
        properties.setProperty("maxRedirections", String.valueOf(maxRedirections));
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
    public RedisClusterClient() {

    }

    @Override
    public void init() {
        if (jedisCluster != null) {
            return;
        }
        datePattern = properties.getProperty("datePattern") == null ? "yyyy-MM-dd HH:mm:ss" : properties.getProperty("datePattern");
        String hosts = properties.getProperty("hosts");
        int maxRedirections = Integer.parseInt(properties.getProperty("maxRedirections"));
        int timeout = Integer.parseInt(properties.getProperty("timeout"));
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(Integer.parseInt(properties.getProperty("maxIdle")));
        poolConfig.setMaxTotal(Integer.parseInt(properties.getProperty("maxTotal")));
        poolConfig.setMaxWaitMillis(Long.parseLong(properties.getProperty("maxWaitMillis")));
        poolConfig.setMinIdle(Integer.parseInt(properties.getProperty("minIdle")));

        poolConfig.setTestWhileIdle(true);
        poolConfig.setMinEvictableIdleTimeMillis(60000);
        poolConfig.setTimeBetweenEvictionRunsMillis(30000);
        poolConfig.setNumTestsPerEvictionRun(-1);

        Set<HostAndPort> set = new HashSet<>();
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
                set.add(new HostAndPort(ip, port));
            }
        }
        String password = properties.getProperty("password");
        if (password != null && !password.trim().equals("")) {
            jedisCluster = new JedisCluster(set, timeout, timeout, maxRedirections, password, poolConfig);
        } else {
            jedisCluster = new JedisCluster(set, timeout, maxRedirections, poolConfig);
        }
    }

    @Override
    public JedisCluster getClient() {
        return jedisCluster;
    }

    @Override
    public void close() {
        try {
            jedisCluster.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        try {
            return command.commond(getClient());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getDatePattern() {
        return datePattern;
    }

    @Override
    @Deprecated
    public List<Object> execPipelineCommand(PipelineCommand pipelineCommand) {
        return null;
    }


}
